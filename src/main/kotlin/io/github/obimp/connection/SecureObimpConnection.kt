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

import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.packet.ObimpPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.handle.common.CommonPacketHandler.Companion.OBIMP_BEX_COM_CLI_LOGIN
import io.github.obimp.util.HashUtils.base64
import io.github.obimp.util.HashUtils.md5
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Secure OBIMP Connection
 * @author Alexander Krysin
 */
class SecureObimpConnection : Connection<WTLD> {
    private val listenerManager = ObimpListenerManager()
    private val socketChannel = SocketChannel.open()
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
                    OctaWord(ByteBuffer.wrap(md5(md5(username + Connection.SALT + password) + serverKey)))
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
            //socketChannel.write(packet.toBytes())
        }
    }

    override fun connect(host: String, port: Int) {
        // TODO: Implement me
    }

    override fun disconnect() {
        // TODO: Implement me
    }

    override fun getListenerManager() = listenerManager
}