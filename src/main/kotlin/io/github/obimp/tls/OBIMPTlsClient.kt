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

package io.github.obimp.tls

import org.bouncycastle.tls.DefaultTlsClient
import org.bouncycastle.tls.ProtocolVersion
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto
import java.security.SecureRandom

/**
 * OBIMP TLS Client
 * @author Alexander Krysin
 */
class OBIMPTlsClient(private val hostname: String) : DefaultTlsClient(BcTlsCrypto(SecureRandom())) {
    override fun getAuthentication() = OBIMPTlsAuthentication(hostname)

    override fun getSupportedVersions(): Array<ProtocolVersion> = ProtocolVersion.TLSv13.downTo(ProtocolVersion.TLSv10)
}