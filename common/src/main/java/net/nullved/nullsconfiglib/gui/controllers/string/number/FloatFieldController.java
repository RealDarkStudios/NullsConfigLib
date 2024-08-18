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
import net.nullved.nullsconfiglib.gui.controllers.slider.FloatSliderController;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class FloatFieldController extends NumberFieldController<Float> {
    private final float min, max;

    public FloatFieldController(IOption<Float> option, float min, float max, Function<Float, Component> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public FloatFieldController(IOption<Float> option, float min, float max) {
        this(option, min, max, FloatSliderController.DEFAULT_FORMATTER);
    }

    public FloatFieldController(IOption<Float> option, Function<Float, Component> formatter) {
        this(option, -Float.MAX_VALUE, Float.MAX_VALUE, formatter);
    }

    public FloatFieldController(IOption<Float> option) {
        this(option, -Float.MAX_VALUE, Float.MAX_VALUE, FloatSliderController.DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static FloatFieldController createInternal(IOption<Float> option, float min, float max, IValueFormatter<Float> formatter) {
        return new FloatFieldController(option, min, max, formatter::format);
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
        option().requestSet((float) value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
