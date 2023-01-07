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
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.ClientHelloPacket
import io.github.obimp.packet.ClientLoginPacket
import io.github.obimp.packet.OBIMPPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.OBIMPPacketHandler
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
    val packetHandler = OBIMPPacketHandler()

    override fun sendPacket(packet: Packet<out DataStructure<*>>) {
        try {
            /*(packet as OBIMPPacket).header.sequence = sequence
            packet.header.contentLength = packet.body.getLength()*/
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
        sendPacket(ClientHelloPacket(username))
    }

    override fun plaintextLogin() {
        sendPacket(ClientLoginPacket(username, password))
    }

    override fun hashLogin(serverKey: ByteArray) {
        sendPacket(ClientLoginPacket(username, password, serverKey))
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
}