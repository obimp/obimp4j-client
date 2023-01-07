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

package io.github.obimp.packet

import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.BLK
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.transports.TransportOption
import java.nio.ByteBuffer
import java.util.Base64

/**
 * @author Alexander Krysin
 */
class ClientTransportsSettings(
    transportItemID: Int,
    password: String,
    serverHostOrIP: String,
    serverPort: Int,
    options: List<TransportOption>?
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_TP, subtype = OBIMP_BEX_TP_CLI_SETTINGS)) {
    init {
        addItem(WTLD(LongWord(0x0001), LongWord(transportItemID)))
        addItem(
            WTLD(LongWord(0x0002), BLK(ByteBuffer.wrap(Base64.getEncoder().encode(password.encodeToByteArray()))))
        )
        addItem(WTLD(LongWord(0x0003), UTF8(serverHostOrIP)))
        addItem(WTLD(LongWord(0x0004), LongWord(serverPort)))
        options?.let { options ->
            val data = ByteBuffer.allocate(options.sumOf { it.length.toInt() } + (options.size * 2))
            for (option in options) {
                data.putShort(option.length)
                data.putShort(option.id)
                data.put(option.type.value)
                data.putInt(option.flag.value)
                data.putShort(option.nameLength)
                data.put(option.name.encodeToByteArray())
                data.putShort(option.valueLength)
                data.put(option.value.encodeToByteArray())
            }
            data.rewind()
            addItem(WTLD(LongWord(0x0005), BLK(data)))
        }
    }
}