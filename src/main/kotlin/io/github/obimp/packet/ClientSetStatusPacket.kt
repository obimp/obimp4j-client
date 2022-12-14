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
import io.github.obimp.data.type.UUID
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.presence.Status
import io.github.obimp.presence.XStatus

/**
 * @author Alexander Krysin
 */
class ClientSetStatusPacket(
    status: Status,
    statusName: String? = null,
    xStatusPictureNumber: Int? = null,
    xStatusDescription: String? = null,
    xStatus: XStatus? = null
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_PRES, subtype = OBIMP_BEX_PRES_CLI_SET_STATUS)) {
    init {
        addItem(WTLD(LongWord(0x0001), LongWord(status.value)))
        statusName?.let { addItem(WTLD(LongWord(0x0002), UTF8(it))) }
        xStatusPictureNumber?.let { addItem(WTLD(LongWord(0x0003), LongWord(it))) }
        xStatusDescription?.let { addItem(WTLD(LongWord(0x0004), UTF8(it))) }
        xStatus?.let { addItem(WTLD(LongWord(0x0005), UUID(it.uuid))) }
    }
}