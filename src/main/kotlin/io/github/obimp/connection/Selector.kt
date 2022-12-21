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

package io.github.obimp.connection

import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import kotlin.concurrent.thread

/**
 * Socket channels selector
 * @author Alexander Krysin
 */
internal object Selector {
    private var selector: Selector = Selector.open()

    fun start() {
        if (!selector.isOpen) {
            selector = Selector.open()
        }
        thread {
            while (selector.isOpen) {
                selector.select {
                    (it.attachment() as Runnable).run()
                }
            }
        }
    }

    fun register(channel: SocketChannel): SelectionKey {
        return channel.register(selector, OP_CONNECT or OP_READ or OP_WRITE)
    }

    fun stop() {
        if (selector.keys().isEmpty()) {
            selector.close()
        }
    }
}