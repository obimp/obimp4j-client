/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.obimp.connection

import io.github.obimp.common.DisconnectReason
import io.github.obimp.connection.Connection.Companion.SALT
import io.github.obimp.connection.input.ObimpInputDataParser
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.ObimpPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.handle.common.CommonPacketHandler.Companion.OBIMP_BEX_COM_CLI_HELLO
import io.github.obimp.packet.handle.common.CommonPacketHandler.Companion.OBIMP_BEX_COM_CLI_LOGIN
import io.github.obimp.packet.handle.ua.UserAvatarsPacketHandler.Companion.OBIMP_BEX_UA_CLI_AVATAR_REQ
import io.github.obimp.util.HashUtil.base64
import io.github.obimp.util.HashUtil.md5
import kotlinx.coroutines.Runnable
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.*
import java.nio.channels.SocketChannel

/**
 * @author Alexander Krysin
 */
class PlainObimpConnection : Connection<WTLD> {
    private val listenerManager = ObimpListenerManager()
    private val socketChannel = SocketChannel.open()
    private var connected = false
    private var sequence = 0
        get() = field++
    override lateinit var username: String
    override lateinit var password: String
    override var connectionListener = object : ConnectionListener<WTLD> {
        override fun readyToHashLogin(serverKey: ByteArray) {
            val login = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
            login.addItem(WTLD(LongWord(0x0001), UTF8(username)))
            login.addItem(
                WTLD(
                    LongWord(0x0002),
                    OctaWord(ByteBuffer.wrap(md5(md5(username + SALT + password) + serverKey)))
                )
            )
            sendPacket(login)
        }

        override fun readyToPlaintextLogin() {
            val login = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
            login.addItem(WTLD(LongWord(0x0001), UTF8(username)))
            login.addItem(WTLD(LongWord(0x0003), BLK(ByteBuffer.wrap(base64(password)))))
            sendPacket(login)
        }

        override fun sendPacket(packet: Packet<WTLD>) {
            packet.header.sequence = sequence
            packet.header.contentLength = packet.body.getLength()
            try {
                socketChannel.write(packet.toBytes())
            } catch (e: Exception) {
                for (commonListener in getListeners<CommonListener>()) {
                    commonListener.onDisconnect(DisconnectReason.NETWORK_ERROR)
                }
            }
        }
    }

    override fun connect(host: String, port: Int) {
        try {
            socketChannel.configureBlocking(false)
            val selectionKey = socketChannel.register(InputDataReader.selector, OP_CONNECT or OP_READ or OP_WRITE)
            selectionKey.attach(Runnable {
                try {
                    if (!connected && selectionKey.isWritable) {
                        connected = true
                        getListeners<CommonListener>().forEach(CommonListener::onConnect)
                        return@Runnable
                    }
                    val buffer = ByteBuffer.allocate(32)
                    var actualSize = socketChannel.read(buffer)
                    while (actualSize > 0) {
                        buffer.rewind()
                        if (actualSize < 32) {
                            val bytes = ByteArray(actualSize)
                            buffer[bytes]
                            ObimpInputDataParser.parseInputData(this@PlainObimpConnection, ByteBuffer.wrap(bytes))
                        } else {
                            ObimpInputDataParser.parseInputData(this@PlainObimpConnection, buffer)
                        }
                        buffer.clear()
                        actualSize = socketChannel.read(buffer)
                    }
                } catch (e: Exception) {
                    for (commonListener in getListeners<CommonListener>()) {
                        commonListener.onDisconnect(DisconnectReason.NETWORK_ERROR)
                    }
                }
            })
            InputDataReader.startIfNeeded()
            socketChannel.connect(InetSocketAddress(host, port))
        } catch (e: Exception) {
            socketChannel.close()
            InputDataReader.stopIfNeeded()
            getListeners<CommonListener>().forEach(CommonListener::onConnectError)
        }
    }

    override fun login(username: String, password: String) {
        this.username = username
        this.password = password
        val hello = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_HELLO)
        hello.addItem(WTLD(LongWord(0x0001), UTF8(username)))
        sendPacket(hello)
    }

    fun loadAvatar(avatarMD5Hash: ByteArray) {
        val avatarRequest = ObimpPacket(OBIMP_BEX_UA, OBIMP_BEX_UA_CLI_AVATAR_REQ)
        avatarRequest.addItem(WTLD(LongWord(0x0001), OctaWord(ByteBuffer.wrap(avatarMD5Hash))))
        sendPacket(avatarRequest)
    }

    override fun disconnect() {
        socketChannel.close()
        InputDataReader.stopIfNeeded()
        for (commonListener in getListeners<CommonListener>()) {
            commonListener.onDisconnect(DisconnectReason.DISCONNECTED_BY_USER)
        }
    }

    override fun getListenerManager() = listenerManager
}