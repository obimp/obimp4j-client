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
import io.github.obimp.connection.input.OBIMPInputDataParser
import io.github.obimp.listener.CommonListener
import java.net.InetSocketAddress
import java.nio.ByteBuffer

/**
 * Plain OBIMP connection
 * @author Alexander Krysin
 */
internal class PlainOBIMPConnection(configuration: ClientConfiguration) : AbstractOBIMPConnection(configuration) {
    override fun connect(hostname: String, port: Int) {
        try {
            channel.configureBlocking(false)
            val selectionKey = Selector.register(channel)
            selectionKey.attach(Runnable {
                try {
                    if (selectionKey.isConnectable) {
                        channel.finishConnect()
                        getListeners<CommonListener>().forEach(CommonListener::onConnect)
                    } else if (selectionKey.isReadable) {
                        val buffer = ByteBuffer.allocate(32)
                        while (channel.read(buffer) > 0) {
                            buffer.flip()
                            val bytes = ByteArray(buffer.remaining())
                            buffer[bytes]
                            buffer.clear()
                            OBIMPInputDataParser.parseInputData(this@PlainOBIMPConnection, ByteBuffer.wrap(bytes))
                        }
                    } else if (selectionKey.isWritable) {
                        while (outputCache.isNotEmpty()) {
                            channel.write(outputCache.removeFirst().toBytes())
                        }
                    }
                } catch (e: Exception) {
                    close()
                    for (commonListener in getListeners<CommonListener>()) {
                        commonListener.onDisconnect(DisconnectReason.NETWORK_ERROR)
                    }
                }
            })
            Selector.start()
            channel.connect(InetSocketAddress(hostname, port))
        } catch (e: Exception) {
            close()
            getListeners<CommonListener>().forEach(CommonListener::onConnectError)
        }
    }
}