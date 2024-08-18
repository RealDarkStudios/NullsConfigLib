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
import net.nullved.nullsconfiglib.api.controller.IDoubleFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.DoubleSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.DoubleFieldController;

public class DoubleFieldControllerBuilder extends AbstractControllerBuilder<Double> implements IDoubleFieldControllerBuilder {
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private IValueFormatter<Double> formatter = DoubleSliderController.DEFAULT_FORMATTER::apply;

    public DoubleFieldControllerBuilder(IOption<Double> option) {
        super(option);
    }

    @Override
    public DoubleFieldControllerBuilder min(Double min) {
        this.min = min;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder max(Double max) {
        this.max = max;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder range(Double min, Double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder formatValue(IValueFormatter<Double> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Double> build() {
        return DoubleFieldController.createInternal(option, min, max, formatter);
    }
}