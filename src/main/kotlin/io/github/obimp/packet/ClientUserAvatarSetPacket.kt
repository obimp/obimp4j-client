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
import io.github.obimp.data.type.OctaWord
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.handle.ua.UserAvatarsPacketHandler.Companion.OBIMP_BEX_UA_CLI_AVATAR_SET
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ClientUserAvatarSetPacket(
    avatarFileMD5Hash: ByteArray? = null,
    avatarFileData: ByteArray? = null,
    clearAvatar: Boolean? = null,
    transportItemID: Int? = null) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_UA, subtype = OBIMP_BEX_UA_CLI_AVATAR_SET)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        if (avatarFileMD5Hash != null && avatarFileData != null) {
            body.content.add(WTLD(LongWord(0x0001), OctaWord(ByteBuffer.wrap(avatarFileMD5Hash))))
            body.content.add(WTLD(LongWord(0x0002), BLK(ByteBuffer.wrap(avatarFileData))))
        } else if (clearAvatar != null) {
            body.content.add(WTLD(LongWord(0x0001)))
        }
        transportItemID?.let { body.content.add(WTLD(LongWord(0x1001), LongWord(it))) }
    }
}