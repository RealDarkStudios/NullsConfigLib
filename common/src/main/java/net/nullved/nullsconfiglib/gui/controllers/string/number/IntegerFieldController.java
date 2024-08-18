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

package net.nullved.nullsconfiglib.gui.controllers.string.number;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.IntegerSliderController;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class IntegerFieldController extends NumberFieldController<Integer> {
    private final int min, max;

    public IntegerFieldController(IOption<Integer> option, int min, int max, Function<Integer, Component> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public IntegerFieldController(IOption<Integer> option, int min, int max) {
        this(option, min, max, IntegerSliderController.DEFAULT_FORMATTER);
    }

    public IntegerFieldController(IOption<Integer> option, Function<Integer, Component> formatter) {
        this(option, -Integer.MAX_VALUE, Integer.MAX_VALUE, formatter);
    }

    public IntegerFieldController(IOption<Integer> option) {
        this(option, -Integer.MAX_VALUE, Integer.MAX_VALUE, IntegerSliderController.DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static IntegerFieldController createInternal(IOption<Integer> option, int min, int max, IValueFormatter<Integer> formatter) {
        return new IntegerFieldController(option, min, max, formatter::format);
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
    }

    @Override
    public String getString() {
        return NUMBER_FORMAT.format(option().pendingValue());
    }

    @Override
    public void setPendingValue(double value) {
        option().requestSet((int) value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
