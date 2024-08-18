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
import net.nullved.nullsconfiglib.gui.controllers.string.IStringController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDropdownController<T> implements IStringController<T> {
    protected final IOption<T> option;
    private final List<String> allowedValues;
    public final boolean allowEmptyValue;
    public final boolean allowAnyValue;

    protected AbstractDropdownController(IOption<T> option, List<String> allowedValues, boolean allowEmptyValue, boolean allowAnyValue) {
        this.option = option;
        this.allowedValues = allowedValues;
        this.allowEmptyValue = allowEmptyValue;
        this.allowAnyValue = allowAnyValue;
    }

    protected AbstractDropdownController(IOption<T> option, List<String> allowedValues) {
        this(option, allowedValues, false, false);
    }

    protected AbstractDropdownController(IOption<T> option) {
        this(option, Collections.emptyList());
    }

    @Override
    public IOption<T> option() {
        return option;
    }

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public List<String> getAllowedValues(String input) {
        List<String> values = new ArrayList<>(allowedValues);
        if (allowEmptyValue && !values.contains("")) values.add("");
        if (allowAnyValue && !input.isBlank() && !allowedValues.contains(input)) {
            values.add(input);
        }
        String currentValue = getString();
        if (allowAnyValue && !allowedValues.contains(currentValue)) {
            values.add(currentValue);
        }
        return values;
    }

    public boolean isValueValid(String value) {
        if (value.isBlank()) return allowEmptyValue;
        return allowAnyValue || getAllowedValues().contains(value);
    }

    protected String getValidValue(String value) {
        return getValidValue(value, 0);
    }
    protected String getValidValue(String value, int offset) {
        if (offset == -1) return getString();

        String valueLowerCase = value.toLowerCase();
        return getAllowedValues(value).stream()
                .filter(val -> val.toLowerCase().contains(valueLowerCase))
                .sorted((s1, s2) -> {
                    String s1LowerCase = s1.toLowerCase();
                    String s2LowerCase = s2.toLowerCase();
                    if (s1LowerCase.startsWith(valueLowerCase) && !s2LowerCase.startsWith(valueLowerCase)) return -1;
                    if (!s1LowerCase.startsWith(valueLowerCase) && s2LowerCase.startsWith(valueLowerCase)) return 1;
                    return s1.compareTo(s2);
                })
                .skip(offset)
                .findFirst()
                .orElseGet(this::getString);
    }
}
