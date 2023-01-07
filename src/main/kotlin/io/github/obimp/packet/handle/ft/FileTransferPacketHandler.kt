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

package io.github.obimp.packet.handle.ft

import io.github.obimp.connection.AbstractOBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_CLI_SRV_CONTROL
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_DIR_PROX_ERROR
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_DIR_PROX_FILE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_DIR_PROX_FILE_DATA
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_DIR_PROX_FILE_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_DIR_PROX_HELLO
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.ft.handler.*

/**
 * @author Alexander Krysin
 */
internal class FileTransferPacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_FT_SRV_PARAMS_REPLY, FileTransferParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REQUEST, SendFileRequestPacketHandler()),
        Pair(OBIMP_BEX_FT_CLI_SRV_SEND_FILE_REPLY, SendFileReplyPacketHandler()),
        Pair(OBIMP_BEX_FT_CLI_SRV_CONTROL, ControlPacketHandler()),
        Pair(OBIMP_BEX_FT_DIR_PROX_ERROR, ErrorPacketHandler()),
        Pair(OBIMP_BEX_FT_DIR_PROX_HELLO, HelloPacketHandler()),
        Pair(OBIMP_BEX_FT_DIR_PROX_FILE, FilePacketHandler()),
        Pair(OBIMP_BEX_FT_DIR_PROX_FILE_REPLY, FileReplyPacketHandler()),
        Pair(OBIMP_BEX_FT_DIR_PROX_FILE_DATA, FileDataPacketHandler())
    )

    override fun handlePacket(connection: AbstractOBIMPConnection, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }
}