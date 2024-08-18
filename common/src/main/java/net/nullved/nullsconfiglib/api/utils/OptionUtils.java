/*
 * NullsConfigLib - A Config Library for Null's Mods
 * Copyright (C) 2024 NullVed
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

package net.nullved.nullsconfiglib.api.utils;

import net.nullved.nullsconfiglib.api.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class OptionUtils {
    public static Stream<IOption<?>> getFlatOptions(INCLGui gui) {
        return gui.categories().stream()
                .flatMap(category -> category.groups().stream())
                .flatMap(group -> group instanceof IListOption<?> list ? Stream.of(list) : group.options().stream());
    }

    public static void consumeOptions(INCLGui nclGui, Function<IOption<?>, Boolean> consumer) {
        for (IConfigCategory category : nclGui.categories()) {
            for (IOptionGroup group : category.groups()) {
                if (group instanceof IListOption<?> list) {
                    if (consumer.apply(list)) return;
                } else {
                    for (IOption<?> option : group.options()) {
                        if (consumer.apply(option)) return;
                    }
                }
            }
        }
    }

    /**
     * Consumes all options, ignoring groups and categories.
     *
     * @see OptionUtils#consumeOptions(INCLGui, Function)
     */
    public static void forEachOptions(INCLGui nclGui, Consumer<IOption<?>> consumer) {
        consumeOptions(nclGui, (opt) -> {
            consumer.accept(opt);
            return false;
        });
    }
}
