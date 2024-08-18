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

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

public class EnumDropdownController<E extends Enum<E>> extends AbstractDropdownController<E> {
    protected final IValueFormatter<E> formatter;

    public EnumDropdownController(IOption<E> option, IValueFormatter<E> formatter) {
        super(option, Arrays.stream(option.pendingValue().getDeclaringClass().getEnumConstants()).map(formatter::format).map(Component::getString).toList());
        this.formatter = formatter;
    }

    @Override
    public String getString() {
        return formatter.format(option().pendingValue()).getString();
    }

    @Override
    public void setFromString(String value) {
        option().requestSet(getEnumFromString(value));
    }

    /**
     * Searches through enum constants for one whose {@link #formatter} result equals {@code value}
     *
     * @return The enum constant associated with the {@code value} or the pending value if none are found
     * @implNote The return value of {@link #formatter} on each enum constant should be unique in order to ensure accuracy
     */
    private E getEnumFromString(String value) {
        value = value.toLowerCase();
        for (E constant : option().pendingValue().getDeclaringClass().getEnumConstants()) {
            if (formatter.format(constant).getString().toLowerCase().equals(value)) return constant;
        }

        return option().pendingValue();
    }

    @Override
    public boolean isValueValid(String value) {
        value = value.toLowerCase();
        for (String constant : getAllowedValues()) {
            if (constant.equals(value)) return true;
        }

        return false;
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return getValidEnumConstants(value)
                .skip(offset)
                .findFirst()
                .orElseGet(this::getString);
    }

    @NotNull
    protected Stream<String> getValidEnumConstants(String value) {
        String valueLowerCase = value.toLowerCase();
        return getAllowedValues().stream()
                .filter(constant -> constant.toLowerCase().contains(valueLowerCase))
                .sorted((s1, s2) -> {
                    String s1LowerCase = s1.toLowerCase();
                    String s2LowerCase = s2.toLowerCase();
                    if (s1LowerCase.startsWith(valueLowerCase) && !s2LowerCase.startsWith(valueLowerCase)) return -1;
                    if (!s1LowerCase.startsWith(valueLowerCase) && s2LowerCase.startsWith(valueLowerCase)) return 1;
                    return s1.compareTo(s2);
                });
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new EnumDropdownControllerElement<>(this, screen, widgetDimension);
    }
}
