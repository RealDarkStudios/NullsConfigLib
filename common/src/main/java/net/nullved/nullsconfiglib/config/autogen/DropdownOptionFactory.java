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

package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.Dropdown;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IDropdownStringControllerBuilder;

public class DropdownOptionFactory extends SimpleOptionFactory<Dropdown, String> {
    @Override
    protected IControllerBuilder<String> createController(Dropdown annotation, IConfigField<String> field, IOptionAccess storage, IOption<String> option) {
        return IDropdownStringControllerBuilder.create(option)
                .values(annotation.values())
                .allowEmptyValue(annotation.allowEmptyValue())
                .allowAnyValue(annotation.allowAnyValue());
    }
}
