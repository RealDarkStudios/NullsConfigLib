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

package net.nullved.nullsconfiglib.gui.controllers.string.number;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.controllers.slider.ISliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.IStringController;
import net.nullved.nullsconfiglib.gui.controllers.string.StringControllerElement;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.function.Function;

public abstract class NumberFieldController<T extends Number> implements ISliderController<T>, IStringController<T> {
    protected static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = DecimalFormatSymbols.getInstance();

    private final IOption<T> option;
    private final IValueFormatter<T> valueFormatter;

    public NumberFieldController(IOption<T> option, Function<T, Component> valueFormatter) {
        this.option = option;
        this.valueFormatter = valueFormatter::apply;
    }

    @Override
    public IOption<T> option() {
        return this.option;
    }

    @Override
    public void setFromString(String value) {
        try {
            setPendingValue(Mth.clamp(NUMBER_FORMAT.parse(value).doubleValue(), min(), max()));
        } catch (ParseException ignore) {
            NullsConfigLib.LOGGER.warn("Failed to parse number: {}", value);
        }
    }

    @Override
    public double pendingValue() {
        return option().pendingValue().doubleValue();
    }

    @Override
    public boolean isInputValid(String input) {
        input = input.replace(DECIMAL_FORMAT_SYMBOLS.getGroupingSeparator() + "", "");
        ParsePosition parsePosition = new ParsePosition(0);
        NUMBER_FORMAT.parse(input, parsePosition);
        return parsePosition.getIndex() == input.length();
    }

    @Override
    public Component formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new StringControllerElement(this, screen, widgetDimension, false);
    }

    @Override
    public double step() {
        return -1;
    }
}
