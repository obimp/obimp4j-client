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
import io.github.obimp.im.EncryptionType
import io.github.obimp.im.MessageType
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ClientMessagePacket(
    accountName: String,
    messageID: Int,
    messageType: MessageType,
    messageData: ByteArray,
    requestAcknowledge: Boolean = false,
    encryptionType: EncryptionType? = null,
    transportItemID: Int? = null
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_IM, subtype = OBIMP_BEX_IM_CLI_MESSAGE)) {
    init {
        addItem(WTLD(LongWord(0x0001), UTF8(accountName)))
        addItem(WTLD(LongWord(0x0002), LongWord(messageID)))
        addItem(WTLD(LongWord(0x0003), LongWord(messageType.type)))
        addItem(WTLD(LongWord(0x0004), BLK(ByteBuffer.wrap(messageData))))
        if (requestAcknowledge) {
            addItem(WTLD(LongWord(0x0005)))
        }
        encryptionType?.let { addItem(WTLD(LongWord(0x0006), LongWord(it.type))) }
        transportItemID?.let { addItem(WTLD(LongWord(0x1001), LongWord(it))) }
    }
}