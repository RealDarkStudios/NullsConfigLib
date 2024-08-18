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

public class DropdownStringControllerElement extends AbstractDropdownControllerElement<String, String> {
    private final DropdownStringController controller;

    public DropdownStringControllerElement(DropdownStringController controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
        super(controller, screen, widgetDimension);
        this.controller = controller;
    }

    @Override
    public List<String> computeMatchingValues() {
        return controller.getAllowedValues(inputField).stream()
                .filter(this::matchingValue)
                .sorted((s1, s2) -> {
                    if (s1.startsWith(inputField) && !s2.startsWith(inputField)) return -1;
                    if (!s1.startsWith(inputField) && s2.startsWith(inputField)) return 1;
                    return s1.compareTo(s2);
                })
                .toList();
    }

    public String getString(String object) {
        return object;
    }
}
