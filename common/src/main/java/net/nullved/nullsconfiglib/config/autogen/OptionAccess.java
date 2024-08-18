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

package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OptionAccess implements IOptionAccess {
    private final Map<String, IOption<?>> storage = new HashMap<>();
    private final Map<String, Consumer<IOption<?>>> scheduledOperations = new HashMap<>();

    @Override
    public @Nullable IOption<?> getOption(String fieldName) {
        return storage.get(fieldName);
    }

    @Override
    public void scheduleOptionOperation(String fieldName, Consumer<IOption<?>> optionConsumer) {
        if (storage.containsKey(fieldName)) {
            optionConsumer.accept(storage.get(fieldName));
        } else {
            scheduledOperations.merge(fieldName, optionConsumer, Consumer::andThen);
        }
    }

    public void putOption(String fieldName, IOption<?> option) {
        storage.put(fieldName, option);

        Consumer<IOption<?>> consumer = scheduledOperations.remove(fieldName);
        if (consumer != null) {
            consumer.accept(option);
        }
    }

    public void checkBadOperations() {
        if (!scheduledOperations.isEmpty()) {
            NullsConfigLib.LOGGER.warn("There are scheduled operations on the `OptionAccess` that tried to reference fields that do not exist. The following have been referenced that do not exist: " + String.join(", ", scheduledOperations.keySet()));
        }
    }
}
