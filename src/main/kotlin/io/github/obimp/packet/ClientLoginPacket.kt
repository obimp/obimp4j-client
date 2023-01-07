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
import io.github.obimp.data.type.UTF8
import io.github.obimp.packet.header.OBIMPHeader
import io.github.obimp.util.HashUtils.base64
import io.github.obimp.util.HashUtils.md5
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ClientLoginPacket(
    accountName: String,
    password: String,
    serverKey: ByteArray? = null
) : OBIMPPacket(OBIMPHeader(type = OBIMP_BEX_COM, subtype = OBIMP_BEX_COM_CLI_LOGIN)) {
    init {
        addItem(WTLD(LongWord(0x0001), UTF8(accountName)))
        when (serverKey) {
            null -> addItem(WTLD(LongWord(0x0003), BLK(ByteBuffer.wrap(base64(password)))))
            else -> {
                var md5 = md5(accountName.lowercase() + SALT + password)
                md5 = md5(md5 + serverKey)
                addItem(WTLD(LongWord(0x0002), OctaWord(ByteBuffer.wrap(md5))))
            }
        }
    }

    companion object {
        internal const val SALT = "OBIMPSALT"
    }
}