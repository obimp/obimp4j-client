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
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.ud.UserDetails

/**
 * @author Alexander Krysin
 */
class ClientUsersDirectoryDetailsUpdatePacket(
    userDetails: UserDetails
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_UD, subtype = OBIMP_BEX_UD_CLI_DETAILS_UPD)) {
    init {
        addItem(WTLD(LongWord(0x0001), UTF8(userDetails.accountName)))
        userDetails.nickname?.let { addItem(WTLD(LongWord(0x0002), UTF8(it))) }
        userDetails.firstName?.let { addItem(WTLD(LongWord(0x0003), UTF8(it))) }
        userDetails.lastName?.let { addItem(WTLD(LongWord(0x0004), UTF8(it))) }
        userDetails.country?.let { addItem(WTLD(LongWord(0x0005), Word(it.code))) }
        userDetails.regionState?.let { addItem(WTLD(LongWord(0x0006), UTF8(it))) }
        userDetails.city?.let { addItem(WTLD(LongWord(0x0007), UTF8(it))) }
        userDetails.zipCode?.let { addItem(WTLD(LongWord(0x0008), UTF8(it))) }
        userDetails.address?.let { addItem(WTLD(LongWord(0x0009), UTF8(it))) }
        userDetails.language?.let { addItem(WTLD(LongWord(0x000A), Word(it.code))) }
        userDetails.additionalLanguage?.let { addItem(WTLD(LongWord(0x000B), Word(it.code))) }
        userDetails.gender?.let { addItem(WTLD(LongWord(0x000C), Byte(it.value))) }
        userDetails.birthday?.let { addItem(WTLD(LongWord(0x0002D), DateTime(it))) }
        userDetails.homepage?.let { addItem(WTLD(LongWord(0x000E), UTF8(it))) }
        userDetails.about?.let { addItem(WTLD(LongWord(0x000F), UTF8(it))) }
        userDetails.interests?.let { addItem(WTLD(LongWord(0x0010), UTF8(it))) }
        userDetails.email?.let { addItem(WTLD(LongWord(0x0011), UTF8(it))) }
        userDetails.additionalEmail?.let { addItem(WTLD(LongWord(0x0012), UTF8(it))) }
        userDetails.homePhone?.let { addItem(WTLD(LongWord(0x0013), UTF8(it))) }
        userDetails.workPhone?.let { addItem(WTLD(LongWord(0x0014), UTF8(it))) }
        userDetails.cellularPhone?.let { addItem(WTLD(LongWord(0x0015), UTF8(it))) }
        userDetails.faxNumber?.let { addItem(WTLD(LongWord(0x0016), UTF8(it))) }
        userDetails.hideOnlineStatus?.let { addItem(WTLD(LongWord(0x0017), Bool(it))) }
        userDetails.company?.let { addItem(WTLD(LongWord(0x0018), UTF8(it))) }
        userDetails.divisionDepartment?.let { addItem(WTLD(LongWord(0x0019), UTF8(it))) }
        userDetails.position?.let { addItem(WTLD(LongWord(0x001A), UTF8(it))) }
        userDetails.transportItemID?.let { addItem(WTLD(LongWord(0x1001), LongWord(it))) }
    }
}