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
import io.github.obimp.data.type.Byte
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.Word
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.presence.Language
import io.github.obimp.ud.Country
import io.github.obimp.ud.Gender
import io.github.obimp.ud.ZodiacSign

/**
 * @author Alexander Krysin
 */
class ClientUsersDirectorySearchPacket(
    accountName: String? = null,
    email: String? = null,
    nickname: String? = null,
    firstName: String? = null,
    lastname: String? = null,
    country: Country? = null,
    city: String? = null,
    language: Language? = null,
    gender: Gender? = null,
    minAge: Int? = null,
    maxAge: Int? = null,
    zodiacSign: ZodiacSign? = null,
    interests: String? = null,
    onlineOnly: Boolean = false,
    transportItemID: Int? = null
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_UD, subtype = OBIMP_BEX_UD_CLI_SEARCH)) {
    init {
        if (accountName != null) {
            addItem(WTLD(LongWord(0x0001), UTF8(accountName)))
        } else if (email != null) {
            addItem(WTLD(LongWord(0x0002), UTF8(email)))
        } else {
            nickname?.let { addItem(WTLD(LongWord(0x0003), UTF8(it))) }
            firstName?.let { addItem(WTLD(LongWord(0x0004), UTF8(it))) }
            lastname?.let { addItem(WTLD(LongWord(0x0005), UTF8(it))) }
            country?.let { addItem(WTLD(LongWord(0x0006), Word(it.code))) }
            city?.let { addItem(WTLD(LongWord(0x0007), UTF8(it))) }
            language?.let { addItem(WTLD(LongWord(0x0008), Word(it.code))) }
            gender?.let { addItem(WTLD(LongWord(0x0009), Byte(it.value))) }
            minAge?.let { addItem(WTLD(LongWord(0x000A), Byte(it.toByte()))) }
            maxAge?.let { addItem(WTLD(LongWord(0x000B), Byte(it.toByte()))) }
            zodiacSign?.let { addItem(WTLD(LongWord(0x000C), Byte(it.value))) }
            interests?.let { addItem(WTLD(LongWord(0x000D), UTF8(it))) }
            if (onlineOnly) {
                addItem(WTLD(LongWord(0x000E)))
            }
        }
        transportItemID?.let { addItem(WTLD(LongWord(0x1001), LongWord(it))) }
    }
}