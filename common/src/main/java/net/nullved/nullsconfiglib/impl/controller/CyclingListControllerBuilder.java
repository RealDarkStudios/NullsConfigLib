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

import com.google.common.collect.ImmutableList;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IListOption;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.ICyclingListControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.cycling.CyclingListController;

public class CyclingListControllerBuilder<T> extends AbstractControllerBuilder<T> implements ICyclingListControllerBuilder<T> {
    private Iterable<? extends T> values;
    private IValueFormatter<T> formatter = null;

    public CyclingListControllerBuilder(IOption<T> option) {
        super(option);
    }

    @Override
    public ICyclingListControllerBuilder<T> values(Iterable<? extends T> values) {
        this.values = values;
        return this;
    }

    @Override
    public ICyclingListControllerBuilder<T> values(T... values) {
        this.values = ImmutableList.copyOf(values);
        return this;
    }

    @Override
    public ICyclingListControllerBuilder<T> formatValue(IValueFormatter<T> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<T> build() {
        return CyclingListController.createInternal(option, values, formatter);
    }
}
