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
import net.nullved.nullsconfiglib.api.controller.ILongFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.LongSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.LongFieldController;

public class LongFieldControllerBuilder extends AbstractControllerBuilder<Long> implements ILongFieldControllerBuilder {
    private long min = Long.MIN_VALUE;
    private long max = Long.MAX_VALUE;
    private IValueFormatter<Long> formatter = LongSliderController.DEFAULT_FORMATTER::apply;

    public LongFieldControllerBuilder(IOption<Long> option) {
        super(option);
    }

    @Override
    public LongFieldControllerBuilder min(Long min) {
        this.min = min;
        return this;
    }

    @Override
    public LongFieldControllerBuilder max(Long max) {
        this.max = max;
        return this;
    }

    @Override
    public LongFieldControllerBuilder range(Long min, Long max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public LongFieldControllerBuilder formatValue(IValueFormatter<Long> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Long> build() {
        return LongFieldController.createInternal(option, min, max, formatter);
    }
}