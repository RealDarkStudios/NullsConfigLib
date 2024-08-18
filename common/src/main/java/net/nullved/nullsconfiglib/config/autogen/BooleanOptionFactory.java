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

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.Boolean;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IBooleanControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;

public class BooleanOptionFactory extends SimpleOptionFactory<Boolean, java.lang.Boolean> {
    @Override
    protected IControllerBuilder<java.lang.Boolean> createController(Boolean annotation, IConfigField<java.lang.Boolean> field, IOptionAccess storage, IOption<java.lang.Boolean> option) {
        IBooleanControllerBuilder builder = IBooleanControllerBuilder.create(option).colored(annotation.colored());

        switch (annotation.formatter()) {
            case ON_OFF -> builder.onOffFormatter();
            case YES_NO -> builder.yesNoFormatter();
            case TRUE_FALSE -> builder.trueFalseFormatter();
            case CUSTOM -> builder.formatValue(v -> Component.translatable(getTranslationKey(field, "format." + v)));
        }

        return builder;
    }
}
