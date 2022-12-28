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
import io.github.obimp.ft.FileTransferFileData
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.handle.ft.FileTransferPacketHandler.Companion.OBIMP_BEX_FT_DIR_PROX_FILE_DATA
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader

/**
 * @author Alexander Krysin
 */
class FileTransferDirectProxiedFileDataPacket(fileData: FileTransferFileData) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_FT, subtype = OBIMP_BEX_FT_DIR_PROX_FILE_DATA)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        body.content.add(WTLD(LongWord(0x0001), UTF8(fileData.accountName)))
        body.content.add(WTLD(LongWord(0x0002), QuadWord(fileData.uniqueFileTransferID)))
        body.content.add(WTLD(LongWord(0x0003), Bool(fileData.lastFile)))
        body.content.add(WTLD(LongWord(0x0004), Bool(fileData.lastPartOfFile)))
        body.content.add(WTLD(LongWord(0x0005), BLK(fileData.fileData)))
    }
}