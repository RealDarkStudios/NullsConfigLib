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

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.FloatField;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IFloatFieldControllerBuilder;

public class FloatFieldOptionFactory extends SimpleOptionFactory<FloatField, Float> {

    @Override
    protected IControllerBuilder<Float> createController(FloatField annotation, IConfigField<Float> field, IOptionAccess storage, IOption<Float> option) {
        return IFloatFieldControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = null;
                    if (v == annotation.min()) key = getTranslationKey(field, "format.min");
                    else if (v == annotation.max()) key = getTranslationKey(field, "format.max");
                    
                    if (key != null && Language.getInstance().has(key)) return Component.translatable(key);
                    return Component.translatable(String.format(annotation.format(), v));
                })
                .range(annotation.min(), annotation.max());
    }
}
