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

import io.github.obimp.connection.input.OBIMPInputDataParser
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.handle.OBIMPPacketHandler
import io.github.obimp.packet.parse.OBIMPPacketParser
import org.bouncycastle.tls.TlsClientProtocol
import java.nio.ByteBuffer
import kotlin.concurrent.thread

/**
 * @author Alexander Krysin
 */
internal object TLSProcessor {
    private val protocols = mutableMapOf<TlsClientProtocol, Pair<SecureOBIMPConnection, Boolean>>()

    fun start() {
        if (protocols.isNotEmpty()) {
            thread {
                while (protocols.isNotEmpty()) {
                    for ((protocol, connectionData) in protocols) {
                        val (connection, isConnected) = connectionData
                        if (protocol.isConnected) {
                            if (!isConnected) {
                                connection.getListeners<CommonListener>().forEach(CommonListener::onConnect)
                            }
                            val availableInputBytes = protocol.availableInputBytes
                            if (availableInputBytes > 0) {
                                val buffer = ByteBuffer.allocate(availableInputBytes)
                                protocol.readInput(buffer, availableInputBytes)
                                buffer.flip()
                                val packets = OBIMPPacketParser.parsePackets(buffer)
                                for (packet in packets) {
                                    connection.packetHandler.handlePacket(connection, packet)
                                }
                                OBIMPInputDataParser.parseInputData(connection, buffer)
                            }
                            connection.writeData()
                        }
                        protocols.replace(protocol, Pair(connection, protocol.isConnected))
                        if (protocol.isClosed) {
                            protocols.remove(protocol)
                        }
                    }
                }
            }
        }
    }

    fun register(protocol: TlsClientProtocol, connection: SecureOBIMPConnection) {
        protocols[protocol] = Pair(connection, protocol.isConnected)
    }
}