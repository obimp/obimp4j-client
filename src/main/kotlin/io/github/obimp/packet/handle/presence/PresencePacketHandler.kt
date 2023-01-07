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

package io.github.obimp.packet.handle.presence

import io.github.obimp.connection.AbstractOBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_CONTACT_ONLINE
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_MAIL_NOTIF
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_OWN_MAIL_URL
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_PARAMS_REPLY
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES_SRV_PRES_INFO
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.presence.handlers.*

/**
 * @author Alexander Krysin
 */
internal class PresencePacketHandler : PacketHandler<WTLD> {
    private val bexSubtypeToPacketHandler = mapOf(
        Pair(OBIMP_BEX_PRES_SRV_PARAMS_REPLY, PresenceParametersReplyPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_CONTACT_ONLINE, ContactOnlinePacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_CONTACT_OFFLINE, ContactOfflinePacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_PRES_INFO, PresenceInfoPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_MAIL_NOTIF, MailNotificationPacketHandler()),
        Pair(OBIMP_BEX_PRES_SRV_OWN_MAIL_URL, OwnMailUrlPacketHandler()),
    )

    override fun handlePacket(connection: AbstractOBIMPConnection, packet: Packet<WTLD>) {
        bexSubtypeToPacketHandler[packet.getSubtype()]?.handlePacket(connection, packet)
    }
}