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

package net.nullved.nullsconfiglib.api;

import net.minecraft.client.OptionInstance;
import net.nullved.nullsconfiglib.impl.GenericBinding;
import net.nullved.nullsconfiglib.mixin.OptionInstanceAccessor;
import org.apache.commons.lang3.Validate;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IBinding<T> {
    void setValue(T value);
    T getValue();
    T defaultValue();

    default <U> IBinding<U> xmap(Function<T, U> to, Function<U, T> from) {
        return IBinding.generic(to.apply(this.defaultValue()), () -> to.apply(getValue()), v -> this.setValue(from.apply(v)));
    }

    static <T> IBinding<T> generic(T def, Supplier<T> getter, Consumer<T> setter) {
        Validate.notNull(def, "`def` must not be null");
        Validate.notNull(getter, "`getter` must not be null");
        Validate.notNull(setter, "`setter` must not be null");

        return new GenericBinding<>(def, getter, setter);
    }

    static <T> IBinding<T> minecraft(OptionInstance<T> minecraftOption) {
        Validate.notNull(minecraftOption, "`minecraftOption` must not be null");

        return new GenericBinding<>(
                ((OptionInstanceAccessor<T>) (Object) minecraftOption).getInitialValue(),
                minecraftOption::get,
                minecraftOption::set
        );
    }

    static <T> IBinding<T> immutable(T value) {
        Validate.notNull(value, "`value` must not be null");

        return new GenericBinding<>(
                value,
                () -> value,
                changed -> {}
        );
    }
}
