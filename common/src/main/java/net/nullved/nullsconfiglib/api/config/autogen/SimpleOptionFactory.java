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

package net.nullved.nullsconfiglib.api.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionDescription;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.config.FieldBackedBinding;
import net.nullved.nullsconfiglib.config.autogen.AutoGenUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public abstract class SimpleOptionFactory<A extends Annotation, T> implements IOptionFactory<A, T> {
    @Override
    public IOption<T> createOption(A annotation, IConfigField<T> field, IOptionAccess optionAccess, List<IOptionFlag> flags) {
        IOption<T> option = IOption.<T>createBuilder()
                .name(this.name(annotation, field, optionAccess))
                .description(v -> this.description(v, annotation, field, optionAccess).build())
                .binding(new FieldBackedBinding<>(field.access(), field.defaultAccess()))
                .controller(opt -> {
                    IControllerBuilder<T> builder = this.createController(annotation, field, optionAccess, opt);

                    AutoGenUtils.addCustomFormatterToController(builder, field.access());

                    return builder;
                })
                .available(this.available(annotation, field, optionAccess))
                .flags(flags)
                .listener((opt, v) -> this.listener(annotation, field, optionAccess, opt, v))
                .build();

        postInit(annotation, field, optionAccess, option);
        return option;
    }

    protected abstract IControllerBuilder<T> createController(A annotation, IConfigField<T> field, IOptionAccess storage, IOption<T> option);

    protected MutableComponent name(A annotation, IConfigField<T> field, IOptionAccess storage) {
        Optional<CustomName> customName = field.access().getAnnotation(CustomName.class);
        return Component.translatable(customName.map(CustomName::value).orElse(this.getTranslationKey(field, null)));
    }

    protected IOptionDescription.Builder description(T value, A annotation, IConfigField<T> field, IOptionAccess storage) {
        IOptionDescription.Builder builder = IOptionDescription.createBuilder();

        String key = this.getTranslationKey(field, "description");
        if (Language.getInstance().has(key)) {
            builder.text(Component.translatable(key));
        } else {
            key += ".";
            int i = 1;
            while (Language.getInstance().has(key + i)) {
                builder.text(Component.translatable(key + i));
                i++;
            }
        }

        field.access().getAnnotation(CustomDescription.class).ifPresent(customDescription -> {
            for (String line: customDescription.value()) {
                builder.text(Component.translatable(line));
            }
        });

        return builder;
    }

    protected boolean available(A annotation, IConfigField<T> field, IOptionAccess storage) {
        return true;
    }

    protected void listener(A annotation, IConfigField<T> field, IOptionAccess storage, IOption<T> option, T value) {
    }

    protected void postInit(A annotation, IConfigField<T> field, IOptionAccess storage, IOption<T> option) {
    }

    protected String getTranslationKey(IConfigField<T> field, @Nullable String suffix) {
        String key = "config.%s.%s.%s".formatted(field.parent().id().getNamespace(), field.parent().id().getPath(), field.access().name());
        if (suffix != null) key += "." + suffix;
        return key;
    }
}
