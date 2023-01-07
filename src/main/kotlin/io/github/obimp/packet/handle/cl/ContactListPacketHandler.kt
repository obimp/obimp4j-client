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

package io.github.obimp.packet.handle.cl

import io.github.obimp.connection.AbstractOBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_BEGIN_UPDATE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_DONE_OFFAUTH
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_END_UPDATE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_ITEM_OPER
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_PARAMS_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL_SRV_VERIFY_REPLY
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.cl.handlers.*

/**
 * @author Alexander Krysin
 */
internal class ContactListPacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_CL_SRV_PARAMS_REPLY, ContactListParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_REPLY, ReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_VERIFY_REPLY, VerifyReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_ADD_ITEM_REPLY, AddItemPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_DEL_ITEM_REPLY, DeleteItemReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_UPD_ITEM_REPLY, UpdateItemReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_CLI_SRV_AUTH_REQUEST, AuthorizationRequestPacketHandler()),
        Pair(OBIMP_BEX_CL_CLI_SRV_AUTH_REPLY, AuthorizationReplyPacketHandler()),
        Pair(OBIMP_BEX_CL_CLI_SRV_AUTH_REVOKE, AuthorizationRevokePacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_DONE_OFFAUTH, DoneOffauthPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_ITEM_OPER, ItemOperationPacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_BEGIN_UPDATE, BeginUpdatePacketHandler()),
        Pair(OBIMP_BEX_CL_SRV_END_UPDATE, EndUpdatePacketHandler())
    )

    override fun handlePacket(connection: AbstractOBIMPConnection, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }
}