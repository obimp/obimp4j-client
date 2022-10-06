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

import io.github.obimp.presence.ClientCapability
import io.github.obimp.presence.ClientFlag
import io.github.obimp.presence.ClientType
import io.github.obimp.presence.Language
import io.github.obimp.util.LibVersion
import io.github.obimp.util.SystemInfoUtils
import io.github.obimp.util.Version

/**
 * OBIMP Client configuration
 * @author Alexander Krysin
 */
object OBIMPClientConfiguration : ClientConfiguration {
    override var clientType: ClientType = ClientType.USER
    override var clientName: String = LibVersion.NAME
    override var clientVersion: Version = LibVersion.VERSION
    override var clientDescription: String? = null
    override var clientLanguage: Language = Language.ENGLISH
    override var clientCapabilities: List<ClientCapability> = listOf(ClientCapability.MSGS_UTF8)
    override var clientOperatingSystemName: String? = SystemInfoUtils.getOperatingSystemTitle()
    override var clientHostname: String? = SystemInfoUtils.getHostname()
    override var clientFlag: ClientFlag? = null
}