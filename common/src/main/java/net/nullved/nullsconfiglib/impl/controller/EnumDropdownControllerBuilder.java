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
import net.nullved.nullsconfiglib.api.controller.IEnumDropdownControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.cycling.EnumController;
import net.nullved.nullsconfiglib.gui.controllers.dropdown.EnumDropdownController;

public class EnumDropdownControllerBuilder<E extends Enum<E>> extends AbstractControllerBuilder<E> implements IEnumDropdownControllerBuilder<E> {
    private IValueFormatter<E> formatter = EnumController.<E>getDefaultFormatter()::apply;

    public EnumDropdownControllerBuilder(IOption<E> option) {
        super(option);
    }

    @Override
    public IEnumDropdownControllerBuilder<E> formatValue(IValueFormatter<E> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<E> build() {
        return new EnumDropdownController<>(option, formatter);
    }
}
