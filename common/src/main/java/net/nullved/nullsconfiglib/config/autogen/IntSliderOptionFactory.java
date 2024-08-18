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
import net.nullved.nullsconfiglib.api.config.autogen.IntSlider;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IIntSliderControllerBuilder;

public class IntSliderOptionFactory extends SimpleOptionFactory<IntSlider, Integer> {

    @Override
    protected IControllerBuilder<Integer> createController(IntSlider annotation, IConfigField<Integer> field, IOptionAccess storage, IOption<Integer> option) {
        return IIntSliderControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = getTranslationKey(field, "format." + v);
                    if (Language.getInstance().has(key)) return Component.translatable(key);

                    key = getTranslationKey(field, "format");

                    if (Language.getInstance().has(key)) return Component.translatable(key, v);
                    return Component.translatable(Integer.toString(v));
                })
                .range(annotation.min(), annotation.max())
                .step(annotation.step());
    }
}
