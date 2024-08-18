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
import net.nullved.nullsconfiglib.api.controller.IDropdownStringControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.dropdown.DropdownStringController;

import java.util.Arrays;
import java.util.List;

public class DropdownStringControllerBuilder extends StringControllerBuilder implements IDropdownStringControllerBuilder {
    private List<String> values;
    private boolean allowEmptyValue = false;
    public boolean allowAnyValue = false;

    public DropdownStringControllerBuilder(IOption<String> option) {
        super(option);
    }

    @Override
    public IDropdownStringControllerBuilder values(List<String> values) {
        this.values = values;
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder values(String... values) {
        this.values = Arrays.asList(values);
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder allowEmptyValue(boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder allowAnyValue(boolean allowAnyValue) {
        this.allowAnyValue = allowAnyValue;
        return this;
    }

    @Override
    public IController<String> build() {
        return new DropdownStringController(option, values, allowEmptyValue, allowAnyValue);
    }
}
