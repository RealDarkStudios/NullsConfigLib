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

package net.nullved.nullsconfiglib.gui.controllers.cycling;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class CyclingListController<T> implements ICyclingController<T> {
    private final IOption<T> option;
    private final IValueFormatter<T> valueFormatter;
    private final ImmutableList<T> values;

    public CyclingListController(IOption<T> option, Iterable<? extends T> values) {
        this(option, values, value -> Component.literal(value.toString()));
    }

    public CyclingListController(IOption<T> option, Iterable<? extends T> values, Function<T, Component> valueFormatter) {
        this.option = option;
        this.valueFormatter = valueFormatter::apply;
        this.values = ImmutableList.copyOf(values);
    }

    @ApiStatus.Internal
    public static <T> CyclingListController<T> createInternal(IOption<T> option, Iterable<? extends T> values, IValueFormatter<T> formatter) {
        return new CyclingListController<>(option, values, formatter::format);
    }

    @Override
    public IOption<T> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    @Override
    public void setPendingValue(int ordinal) {
        option().requestSet(values.get(ordinal));
    }

    @Override
    public int getPendingValue() {
        return values.indexOf(option().pendingValue());
    }

    @Override
    public int getCycleLength() {
        return values.size();
    }
}
