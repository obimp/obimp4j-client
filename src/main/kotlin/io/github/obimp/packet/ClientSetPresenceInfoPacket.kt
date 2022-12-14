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
import io.github.obimp.data.type.LongWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.data.type.VersionQuadWord
import io.github.obimp.data.type.Word
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.presence.PresenceInfo

/**
 * @author Alexander Krysin
 */
class ClientSetPresenceInfoPacket(
    presenceInfo: PresenceInfo
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_PRES, subtype = OBIMP_BEX_PRES_CLI_SET_PRES_INFO)) {
    init {
        addItem(
            WTLD(
                LongWord(0x0001),
                *presenceInfo.clientCapabilities.map { Word(it.capability) }.toTypedArray()
            )
        )
        addItem(WTLD(LongWord(0x0002), Word(presenceInfo.clientType.value)))
        addItem(WTLD(LongWord(0x0003), UTF8(presenceInfo.clientName)))
        addItem(WTLD(LongWord(0x0004), VersionQuadWord(presenceInfo.clientVersion)))
        addItem(WTLD(LongWord(0x0005), Word(presenceInfo.clientLanguage.code)))
        presenceInfo.clientOperatingSystemName?.let { addItem(WTLD(LongWord(0x0006), UTF8(it))) }
        presenceInfo.clientDescription?.let { addItem(WTLD(LongWord(0x0007), UTF8(it))) }
        presenceInfo.clientFlag?.let { addItem(WTLD(LongWord(0x0008), LongWord(it.flag))) }
        presenceInfo.clientHostName?.let { addItem(WTLD(LongWord(0x0009), UTF8(it))) }
    }
}