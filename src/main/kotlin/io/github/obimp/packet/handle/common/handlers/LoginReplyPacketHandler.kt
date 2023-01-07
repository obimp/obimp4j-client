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

package io.github.obimp.packet.handle.common.handlers

import io.github.obimp.common.LoginError
import io.github.obimp.connection.AbstractOBIMPConnection
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.structure.readDataType
import io.github.obimp.data.structure.readDataTypeList
import io.github.obimp.data.type.Word
import io.github.obimp.listener.CommonListener
import io.github.obimp.packet.*
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_FT
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_IM
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_PRES
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_TP
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_UA
import io.github.obimp.packet.OBIMPPacket.Companion.OBIMP_BEX_UD
import io.github.obimp.packet.handle.PacketHandler

/**
 * @author Alexander Krysin
 */
internal class LoginReplyPacketHandler : PacketHandler<WTLD> {
    override fun handlePacket(connection: AbstractOBIMPConnection, packet: Packet<WTLD>) {
        val wtld = packet.nextItem()
        if (wtld.getType() == 0x0001) {
            val errorCode = wtld.readDataType<Word>().value
            for (cl in connection.getListeners<CommonListener>()) {
                cl.onLoginError(LoginError.byCode(errorCode))
            }
        }
        if (wtld.getType() == 0x0002) {
            connection.getListeners<CommonListener>().forEach(CommonListener::onLogin)
            val data = wtld.readDataTypeList<Word>()
            val serverSupportedBexs = mutableMapOf<Short, Short>()
            var i = 0
            while (i < data.size) {
                val bexType = data[i++].value
                val maximalBexSubtype = data[i++].value
                serverSupportedBexs[bexType] = maximalBexSubtype
            }
            for (bexType in serverSupportedBexs.keys) {
                when (bexType) {
                    OBIMP_BEX_CL -> connection.sendPacket(ClientContactListParamsPacket())
                    OBIMP_BEX_PRES -> connection.sendPacket(ClientPresenceParamsPacket())
                    OBIMP_BEX_IM -> connection.sendPacket(ClientInstantMessagingParamsPacket())
                    OBIMP_BEX_UD -> connection.sendPacket(ClientUsersDirectoryParamsPacket())
                    OBIMP_BEX_UA -> connection.sendPacket(ClientUserAvatarsParamsPacket())
                    OBIMP_BEX_FT -> connection.sendPacket(ClientFileTransferParamsPacket())
                    OBIMP_BEX_TP -> connection.sendPacket(ClientTransportsParamsPacket())
                }
            }
            connection.sendPacket(ClientRequestPresenceInfoPacket())
        }
    }
}