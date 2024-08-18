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

package net.nullved.nullsconfiglib.impl;

import net.nullved.nullsconfiglib.api.IBinding;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenericBinding<T> implements IBinding<T> {
    private final T def;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public GenericBinding(T def, Supplier<T> getter, Consumer<T> setter) {
        this.def = def;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void setValue(T value) {
        setter.accept(value);
    }

    @Override
    public T getValue() {
        return getter.get();
    }

    @Override
    public T defaultValue() {
        return def;
    }
}
