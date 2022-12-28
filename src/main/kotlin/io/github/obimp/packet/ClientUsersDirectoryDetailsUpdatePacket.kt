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
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_UD
import io.github.obimp.packet.handle.ud.UsersDirectoryPacketHandler.Companion.OBIMP_BEX_UD_CLI_DETAILS_UPD
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.ud.UserDetails

/**
 * @author Alexander Krysin
 */
class ClientUsersDirectoryDetailsUpdatePacket(userDetails: UserDetails) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_UD, subtype = OBIMP_BEX_UD_CLI_DETAILS_UPD)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        body.content.add(WTLD(LongWord(0x0001), UTF8(userDetails.accountName)))
        userDetails.nickname?.let { body.content.add(WTLD(LongWord(0x0002), UTF8(it))) }
        userDetails.firstName?.let { body.content.add(WTLD(LongWord(0x0003), UTF8(it))) }
        userDetails.lastName?.let { body.content.add(WTLD(LongWord(0x0004), UTF8(it))) }
        userDetails.country?.let { body.content.add(WTLD(LongWord(0x0005), Word(it.code))) }
        userDetails.regionState?.let { body.content.add(WTLD(LongWord(0x0006), UTF8(it))) }
        userDetails.city?.let { body.content.add(WTLD(LongWord(0x0007), UTF8(it))) }
        userDetails.zipCode?.let { body.content.add(WTLD(LongWord(0x0008), UTF8(it))) }
        userDetails.address?.let { body.content.add(WTLD(LongWord(0x0009), UTF8(it))) }
        userDetails.language?.let { body.content.add(WTLD(LongWord(0x000A), Word(it.code))) }
        userDetails.additionalLanguage?.let { body.content.add(WTLD(LongWord(0x000B), Word(it.code))) }
        userDetails.gender?.let { body.content.add(WTLD(LongWord(0x000C), Byte(it.value))) }
        userDetails.birthday?.let { body.content.add(WTLD(LongWord(0x0002D), DateTime(it))) }
        userDetails.homepage?.let { body.content.add(WTLD(LongWord(0x000E), UTF8(it))) }
        userDetails.about?.let { body.content.add(WTLD(LongWord(0x000F), UTF8(it))) }
        userDetails.interests?.let { body.content.add(WTLD(LongWord(0x0010), UTF8(it))) }
        userDetails.email?.let { body.content.add(WTLD(LongWord(0x0011), UTF8(it))) }
        userDetails.additionalEmail?.let { body.content.add(WTLD(LongWord(0x0012), UTF8(it))) }
        userDetails.homePhone?.let { body.content.add(WTLD(LongWord(0x0013), UTF8(it))) }
        userDetails.workPhone?.let { body.content.add(WTLD(LongWord(0x0014), UTF8(it))) }
        userDetails.cellularPhone?.let { body.content.add(WTLD(LongWord(0x0015), UTF8(it))) }
        userDetails.faxNumber?.let { body.content.add(WTLD(LongWord(0x0016), UTF8(it))) }
        userDetails.hideOnlineStatus?.let { body.content.add(WTLD(LongWord(0x0017), Bool(it))) }
        userDetails.company?.let { body.content.add(WTLD(LongWord(0x0018), UTF8(it))) }
        userDetails.divisionDepartment?.let { body.content.add(WTLD(LongWord(0x0019), UTF8(it))) }
        userDetails.position?.let { body.content.add(WTLD(LongWord(0x001A), UTF8(it))) }
        userDetails.transportItemID?.let { body.content.add(WTLD(LongWord(0x1001), LongWord(it))) }
    }
}