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
import net.nullved.nullsconfiglib.api.controller.IDoubleSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.DoubleSliderController;

public class DoubleSliderControllerBuilder extends AbstractControllerBuilder<Double> implements IDoubleSliderControllerBuilder {
    private double min, max, step;
    private IValueFormatter<Double> formatter = DoubleSliderController.DEFAULT_FORMATTER::apply;

    public DoubleSliderControllerBuilder(IOption<Double> option) {
        super(option);
    }

    @Override
    public IDoubleSliderControllerBuilder range(Double min, Double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IDoubleSliderControllerBuilder step(Double step) {
        this.step = step;
        return this;
    }

    @Override
    public IDoubleSliderControllerBuilder formatValue(IValueFormatter<Double> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Double> build() {
        return DoubleSliderController.createInternal(option, min, max, step, formatter);
    }
}