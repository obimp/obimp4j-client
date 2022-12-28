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
import io.github.obimp.data.type.QuadWord
import io.github.obimp.data.type.UTF8
import io.github.obimp.ft.SendFileRequest
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.handle.ft.FileTransferPacketHandler.Companion.OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader

/**
 * @author Alexander Krysin
 */
class ClientServerSendFileRequest(sendFileRequest: SendFileRequest) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_FT, subtype = OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        body.content.add(WTLD(LongWord(0x0001), UTF8(sendFileRequest.accountName)))
        body.content.add(WTLD(LongWord(0x0002), QuadWord(sendFileRequest.uniqueFileTransferID)))
        body.content.add(WTLD(LongWord(0x0003), LongWord(sendFileRequest.filesCount)))
        body.content.add(WTLD(LongWord(0x0004), QuadWord(sendFileRequest.totalSize)))
        body.content.add(WTLD(LongWord(0x0005), UTF8(sendFileRequest.firstFileName)))
        sendFileRequest.senderClientIP?.let { body.content.add(WTLD(LongWord(0x0006), UTF8(it))) }
        sendFileRequest.senderClientPortNumber?.let { body.content.add(WTLD(LongWord(0x0007), LongWord(it))) }
        sendFileRequest.proxyServerHostOrIP?.let { body.content.add(WTLD(LongWord(0x0008), UTF8(it))) }
        sendFileRequest.proxyServerPortNumber?.let { body.content.add(WTLD(LongWord(0x0009), LongWord(it))) }
    }
}