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

package net.nullved.nullsconfiglib.gui.controllers.string;

import net.nullved.nullsconfiglib.api.IOption;

public class StringController implements IStringController<String> {
    private final IOption<String> option;

    public StringController(IOption<String> option) {
        this.option = option;
    }

    @Override
    public IOption<String> option() {
        return option;
    }

    @Override
    public String getString() {
        return option().pendingValue();
    }

    @Override
    public void setFromString(String value) {
        option().requestSet(value);
    }
}
