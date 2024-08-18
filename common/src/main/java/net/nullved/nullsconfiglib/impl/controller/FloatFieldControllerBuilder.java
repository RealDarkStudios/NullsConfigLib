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

package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IFloatFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.FloatSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.FloatFieldController;

public class FloatFieldControllerBuilder extends AbstractControllerBuilder<Float> implements IFloatFieldControllerBuilder {
    private float min = Float.MIN_VALUE;
    private float max = Float.MAX_VALUE;
    private IValueFormatter<Float> formatter = FloatSliderController.DEFAULT_FORMATTER::apply;

    public FloatFieldControllerBuilder(IOption<Float> option) {
        super(option);
    }

    @Override
    public FloatFieldControllerBuilder min(Float min) {
        this.min = min;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder max(Float max) {
        this.max = max;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder range(Float min, Float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder formatValue(IValueFormatter<Float> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Float> build() {
        return FloatFieldController.createInternal(option, min, max, formatter);
    }
}