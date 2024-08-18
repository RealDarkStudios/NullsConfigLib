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
import net.nullved.nullsconfiglib.api.controller.IIntSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.IntegerSliderController;

public class IntSliderControllerBuilder extends AbstractControllerBuilder<Integer> implements IIntSliderControllerBuilder {
    private int min, max, step;
    private IValueFormatter<Integer> formatter = IntegerSliderController.DEFAULT_FORMATTER::apply;

    public IntSliderControllerBuilder(IOption<Integer> option) {
        super(option);
    }

    @Override
    public IIntSliderControllerBuilder range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IIntSliderControllerBuilder step(Integer step) {
        this.step = step;
        return this;
    }

    @Override
    public IIntSliderControllerBuilder formatValue(IValueFormatter<Integer> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Integer> build() {
        return IntegerSliderController.createInternal(option, min, max, step, formatter);
    }
}
