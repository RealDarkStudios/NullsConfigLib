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

package net.nullved.nullsconfiglib.config;

import net.nullved.nullsconfiglib.api.IBinding;
import net.nullved.nullsconfiglib.api.config.IFieldAccess;
import net.nullved.nullsconfiglib.api.config.IReadOnlyFieldAccess;

public record FieldBackedBinding<T>(IFieldAccess<T> field, IReadOnlyFieldAccess<T> defaultField) implements IBinding<T> {
    @Override
    public T getValue() {
        return field.get();
    }

    @Override
    public void setValue(T value) {
        field.set(value);
    }

    @Override
    public T defaultValue() {
        return defaultField.get();
    }
}
