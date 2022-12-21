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

package io.github.obimp

import io.github.obimp.connection.PlainOBIMPConnection
import io.github.obimp.connection.SecureOBIMPConnection
import io.github.obimp.listener.*

/**
 * OBIMP Client
 * @author Alexander Krysin
 */
class OBIMPClient(secure: Boolean, configure: ClientConfiguration.() -> Unit = {}) : Client {
    private val configuration = OBIMPClientConfiguration()
    private val connection = when {
        secure -> SecureOBIMPConnection(configuration)
        else -> PlainOBIMPConnection(configuration)
    }

    init {
        configuration.configure()
    }

    fun addCommonListener(listener: CommonListener) = connection.addListener<CommonListener>(listener)

    fun addContactListListener(listener: ContactListListener) = connection.addListener<ContactListListener>(listener)

    fun addPresenceInfoListener(listener: PresenceInfoListener) = connection.addListener<PresenceInfoListener>(listener)

    fun addInstantMessagingListener(listener: InstantMessagingListener) =
        connection.addListener<InstantMessagingListener>(listener)

    fun addUsersDirectoryListener(listener: UsersDirectoryListener) =
        connection.addListener<UsersDirectoryListener>(listener)

    fun addUserAvatarsListener(listener: UserAvatarsListener) = connection.addListener<UserAvatarsListener>(listener)

    fun addFileTransferListener(listener: FileTransferListener) = connection.addListener<FileTransferListener>(listener)

    fun addTransportsListener(listener: TransportsListener) = connection.addListener<TransportsListener>(listener)

    fun removeCommonListener(listener: CommonListener) = connection.removeListener<CommonListener>(listener)

    fun removeContactListListener(listener: ContactListListener) =
        connection.removeListener<ContactListListener>(listener)

    fun removePresenceInfoListener(listener: PresenceInfoListener) =
        connection.removeListener<PresenceInfoListener>(listener)

    fun removeInstantMessagingListener(listener: InstantMessagingListener) =
        connection.removeListener<InstantMessagingListener>(listener)

    fun removeUsersDirectoryListener(listener: UsersDirectoryListener) =
        connection.removeListener<UsersDirectoryListener>(listener)

    fun removeUserAvatarsListener(listener: UserAvatarsListener) =
        connection.removeListener<UserAvatarsListener>(listener)

    fun removeFileTransferListener(listener: FileTransferListener) =
        connection.removeListener<FileTransferListener>(listener)

    fun removeTransportsListener(listener: TransportsListener) = connection.removeListener<TransportsListener>(listener)

    override fun connect(hostname: String, port: Int) {
        connection.connect(hostname, port)
    }

    override fun login(username: String, password: String) {
        connection.login(username, password)
    }
}