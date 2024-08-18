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
import net.nullved.nullsconfiglib.api.controller.IBooleanControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.BooleanController;
import org.apache.commons.lang3.Validate;

public class BooleanControllerBuilder extends AbstractControllerBuilder<Boolean> implements IBooleanControllerBuilder {
    private boolean colored = false;
    private IValueFormatter<Boolean> formatter = BooleanController.ON_OFF_FORMATTER::apply;

    public BooleanControllerBuilder(IOption<Boolean> option) {
        super(option);
    }

    @Override
    public IBooleanControllerBuilder colored(boolean colored) {
        this.colored = colored;
        return this;
    }

    @Override
    public IBooleanControllerBuilder formatValue(IValueFormatter<Boolean> formatter) {
        Validate.notNull(formatter, "`formatter` must not be null");

        this.formatter = formatter;
        return this;
    }

    @Override
    public IBooleanControllerBuilder onOffFormatter() {
        this.formatter = BooleanController.ON_OFF_FORMATTER::apply;
        return this;
    }

    @Override
    public IBooleanControllerBuilder yesNoFormatter() {
        this.formatter = BooleanController.YES_NO_FORMATTER::apply;
        return this;
    }

    @Override
    public IBooleanControllerBuilder trueFalseFormatter() {
        this.formatter = BooleanController.TRUE_FALSE_FORMATTER::apply;
        return this;
    }

    @Override
    public IController<Boolean> build() {
        return BooleanController.createInternal(option, formatter, colored);
    }
}
