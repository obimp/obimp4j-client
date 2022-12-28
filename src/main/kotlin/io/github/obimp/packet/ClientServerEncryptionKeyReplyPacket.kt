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
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.handle.im.InstantMessagingPacketHandler.Companion.OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ClientServerEncryptionKeyReplyPacket(
    accountName: String,
    encryptionType: EncryptionType,
    publicKey: ByteArray? = null
) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_IM, subtype = OBIMP_BEX_IM_CLI_SRV_ENCRYPT_KEY_REPLY)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        body.content.add(WTLD(LongWord(0x0001), UTF8(accountName)))
        body.content.add(WTLD(LongWord(0x0002), LongWord(encryptionType.type)))
        publicKey?.let { body.content.add(WTLD(LongWord(0x0003), BLK(ByteBuffer.wrap(it)))) }
    }
}