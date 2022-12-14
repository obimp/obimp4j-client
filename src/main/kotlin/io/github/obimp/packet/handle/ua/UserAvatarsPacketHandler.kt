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

package io.github.obimp.packet.handle.ua

import io.github.obimp.connection.AbstractOBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_UA_SRV_AVATAR_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_UA_SRV_PARAMS_REPLY
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.ua.handlers.AvatarReplyPacketHandler
import io.github.obimp.packet.handle.ua.handlers.AvatarSetReplyPacketHandler
import io.github.obimp.packet.handle.ua.handlers.UserAvatarsParametersReplyPacketHandler

/**
 * @author Alexander Krysin
 */
internal class UserAvatarsPacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_UA_SRV_PARAMS_REPLY, UserAvatarsParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_UA_SRV_AVATAR_REPLY, AvatarReplyPacketHandler()),
        Pair(OBIMP_BEX_UA_SRV_AVATAR_SET_REPLY, AvatarSetReplyPacketHandler())
    )

    override fun handlePacket(connection: AbstractOBIMPConnection, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }
}