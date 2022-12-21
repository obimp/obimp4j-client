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

import io.github.obimp.ClientConfiguration
import io.github.obimp.common.DisconnectReason
import io.github.obimp.data.structure.DataStructure
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.OctaWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.ObimpPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_COM
import io.github.obimp.packet.handle.common.CommonPacketHandler.Companion.OBIMP_BEX_COM_CLI_HELLO
import io.github.obimp.packet.handle.common.CommonPacketHandler.Companion.OBIMP_BEX_COM_CLI_LOGIN
import io.github.obimp.util.HashUtils
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Abstract OBIMP connection
 * @author Alexander Krysin
 */
internal abstract class AbstractOBIMPConnection(
    internal val configuration: ClientConfiguration
) : OBIMPConnection, ListenerManager() {
    protected val channel: SocketChannel = SocketChannel.open()
    protected val outputCache = mutableListOf<Packet<out DataStructure<*>>>()
    private var sequence = 0
        get() = field++
    internal lateinit var username: String
    private lateinit var password: String

    override fun sendPacket(packet: Packet<out DataStructure<*>>) {
        try {
            packet.header.sequence = sequence
            packet.header.contentLength = packet.body.getLength()
            outputCache.add(packet)
        } catch (e: Exception) {
            for (commonListener in getListeners<CommonListener>()) {
                commonListener.onDisconnect(DisconnectReason.NETWORK_ERROR)
            }
        }
    }

    override fun login(username: String, password: String) {
        this.username = username
        this.password = password
        val packet = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_HELLO)
        packet.addItem(WTLD(LongWord(0x0001), UTF8(username)))
        sendPacket(packet)
    }

    override fun plaintextLogin() {
        val login = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
        login.addItem(WTLD(LongWord(0x0001), UTF8(username)))
        login.addItem(WTLD(LongWord(0x0003), BLK(ByteBuffer.wrap(HashUtils.base64(password)))))
        sendPacket(login)
    }

    override fun hashLogin(serverKey: ByteArray) {
        val login = ObimpPacket(OBIMP_BEX_COM, OBIMP_BEX_COM_CLI_LOGIN)
        login.addItem(WTLD(LongWord(0x0001), UTF8(username)))
        login.addItem(
            WTLD(
                LongWord(0x0002),
                OctaWord(ByteBuffer.wrap(HashUtils.md5(HashUtils.md5(username + SALT + password) + serverKey)))
            )
        )
        sendPacket(login)
    }

    override fun disconnect() {
        close()
        for (commonListener in getListeners<CommonListener>()) {
            commonListener.onDisconnect(DisconnectReason.DISCONNECTED_BY_USER)
        }
    }

    protected fun close() {
        channel.close()
        Selector.stop()
    }

    companion object {
        internal const val SALT = "OBIMPSALT"
    }
}