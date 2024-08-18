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

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.List;

public class DropdownStringController extends AbstractDropdownController<String> {
    public DropdownStringController(IOption<String> option, List<String> allowedValues, boolean allowEmptyValue, boolean allowAnyValue) {
        super(option, allowedValues, allowEmptyValue, allowAnyValue);
    }

    @Override
    public String getString() {
        return option().pendingValue();
    }

    @Override
    public void setFromString(String value) {
        option().requestSet(getValidValue(value));
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new DropdownStringControllerElement(this, screen, widgetDimension);
    }
}
