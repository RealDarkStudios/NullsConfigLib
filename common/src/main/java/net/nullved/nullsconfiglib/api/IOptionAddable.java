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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public interface IOptionAddable {
    IOptionAddable option(@NotNull IOption<?> option);

    default IOptionAddable option(@NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
        return option(optionSupplier.get());
    }

    default IOptionAddable optionIf(boolean condition, @NotNull IOption<?> option) {
        return condition ? option(option) : this;
    }

    /**
     * Adds an option to an abstract builder if a condition is met.
     * To construct an option, use {@link IOption#createBuilder()}
     * @param condition only if true is the option added
     * @param optionSupplier to be called to initialise the option. called immediately only if condition is true
     * @return this
     */
    default IOptionAddable optionIf(boolean condition, @NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
        return condition ? option(optionSupplier) : this;
    }

    /**
     * Adds multiple options to an abstract builder.
     * To construct an option, use {@link IOption#createBuilder()}
     */
    IOptionAddable options(@NotNull Collection<? extends IOption<?>> options);
}
