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

package io.github.obimp.packet.handle.im.handlers

import io.github.obimp.connection.Connection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.packet.ObimpPacket
import io.github.obimp.packet.Packet
import io.github.obimp.packet.handle.ObimpPacketHandler.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.handle.PacketHandler
import io.github.obimp.packet.handle.im.InstantMessagingPacketHandler.Companion.OBIMP_BEX_IM_CLI_DEL_OFFLINE

/**
 * @author Alexander Krysin
 */
class DoneOfflinePacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: Connection<WTLD>, packet: Packet<WTLD>) {
        connection.sendPacket(ObimpPacket(OBIMP_BEX_IM, OBIMP_BEX_IM_CLI_DEL_OFFLINE))
    }
}