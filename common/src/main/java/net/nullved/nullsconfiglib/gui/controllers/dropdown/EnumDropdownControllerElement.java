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

package net.nullved.nullsconfiglib.gui.controllers.dropdown;

import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.List;

public class EnumDropdownControllerElement<E extends Enum<E>> extends AbstractDropdownControllerElement<E, String> {
    private final EnumDropdownController<E> controller;

    public EnumDropdownControllerElement(EnumDropdownController<E> controller, NCLScreen screen, Dimension<Integer> dim) {
        super(controller, screen, dim);
        this.controller = controller;
    }

    @Override
    public List<String> computeMatchingValues() {
        return controller.getValidEnumConstants(inputField).toList();
    }

    @Override
    public String getString(String object) {
        return object;
    }
}
