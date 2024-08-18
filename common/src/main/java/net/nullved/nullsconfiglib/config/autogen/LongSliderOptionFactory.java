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
import net.nullved.nullsconfiglib.api.config.autogen.LongSlider;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.ILongSliderControllerBuilder;

public class LongSliderOptionFactory extends SimpleOptionFactory<LongSlider, Long> {

    @Override
    protected IControllerBuilder<Long> createController(LongSlider annotation, IConfigField<Long> field, IOptionAccess storage, IOption<Long> option) {
        return ILongSliderControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = getTranslationKey(field, "format." + v);
                    if (Language.getInstance().has(key)) return Component.translatable(key);

                    key = getTranslationKey(field, "format");

                    if (Language.getInstance().has(key)) return Component.translatable(key, v);
                    return Component.translatable(Long.toString(v));
                })
                .range(annotation.min(), annotation.max())
                .step(annotation.step());
    }
}
