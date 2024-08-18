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

import java.util.Objects;

public class SafeBinding<T> implements IBinding<T> {
    private final IBinding<T> binding;

    public SafeBinding(IBinding<T> binding) {
        this.binding = binding;
    }

    @Override
    public T getValue() {
        return Objects.requireNonNull(binding.getValue());
    }

    @Override
    public void setValue(T value) {
        binding.setValue(Objects.requireNonNull(value));
    }

    @Override
    public T defaultValue() {
        return Objects.requireNonNull(binding.defaultValue());
    }
}