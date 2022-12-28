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

import io.github.obimp.cl.*
import io.github.obimp.data.structure.STLD
import io.github.obimp.data.structure.WTLD
import io.github.obimp.data.type.*
import io.github.obimp.data.type.Byte
import io.github.obimp.packet.body.Body
import io.github.obimp.packet.body.OBIMPBody
import io.github.obimp.packet.handle.OBIMPPacketHandler.Companion.OBIMP_BEX_CL
import io.github.obimp.packet.handle.cl.ContactListPacketHandler.Companion.OBIMP_BEX_CL_CLI_UPD_ITEM
import io.github.obimp.packet.header.Header
import io.github.obimp.packet.header.OBIMPHeader
import java.nio.ByteBuffer

/**
 * @author Alexander Krysin
 */
class ClientContactListUpdItemPacket(
    itemID: Int,
    newParentGroupID: Int? = null,
    item: ContactListItem? = null
) : Packet<WTLD> {
    override var header: Header = OBIMPHeader(type = OBIMP_BEX_CL, subtype = OBIMP_BEX_CL_CLI_UPD_ITEM)
    override var body: Body<WTLD> = OBIMPBody()

    init {
        body.content.add(WTLD(LongWord(0x0001), LongWord(itemID)))
        newParentGroupID?.let { body.content.add(WTLD(LongWord(0x0002), LongWord(newParentGroupID))) }
        item?.let {
            val itemData = mutableListOf<STLD>()
            when (it.type) {
                ContactListItemType.GROUP -> {
                    val group = item as Group
                    itemData.add(STLD(Word(0x0001), UTF8(group.name)))
                }
                ContactListItemType.CONTACT -> {
                    val contact = item as Contact
                    itemData.add(STLD(Word(0x0002), UTF8(contact.accountName)))
                    itemData.add(STLD(Word(0x0003), UTF8(contact.contactName)))
                    itemData.add(STLD(Word(0x0004), Byte(contact.privacyType.value)))
                    itemData.add(STLD(Word(0x0005)))
                    contact.transportItemId?.let {
                        itemData.add(STLD(Word(0x1001), LongWord(it)))
                    }
                }
                ContactListItemType.TRANSPORT -> {
                    val transport = item as Transport
                    itemData.add(STLD(Word(0x1002), UUID(transport.uuid)))
                    itemData.add(STLD(Word(0x1003), UTF8(transport.transportAccountName)))
                    itemData.add(STLD(Word(0x1004), UTF8(transport.transportFriendlyName)))
                }
                ContactListItemType.NOTE -> {
                    val note = item as Note
                    itemData.add(STLD(Word(0x2001), UTF8(note.noteName)))
                    itemData.add(STLD(Word(0x2002), Byte(note.noteType.value)))
                    note.noteText?.let { itemData.add(STLD(Word(0x2003), UTF8(it))) }
                    note.noteDate?.let { itemData.add(STLD(Word(0x2004), DateTime(it))) }
                    note.notePictureMD5Hash?.let { itemData.add(STLD(Word(0x2005), OctaWord(ByteBuffer.wrap(it)))) }
                }
            }
            body.content.add(WTLD(LongWord(0x0003), *itemData.toTypedArray()))
        }
    }
}