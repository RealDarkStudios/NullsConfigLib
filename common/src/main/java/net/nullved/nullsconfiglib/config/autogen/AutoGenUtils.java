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
import net.nullved.nullsconfiglib.api.config.IReadOnlyFieldAccess;
import net.nullved.nullsconfiglib.api.config.autogen.CustomFormat;
import net.nullved.nullsconfiglib.api.config.autogen.FormatTranslation;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormattableController;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class AutoGenUtils {
    public static <T> void addCustomFormatterToController(IControllerBuilder<T> controller, IReadOnlyFieldAccess<T> field) {
        Optional<CustomFormat> formatter = field.getAnnotation(CustomFormat.class);
        Optional<FormatTranslation> translation = field.getAnnotation(FormatTranslation.class);

        if (formatter.isPresent() && translation.isPresent()) {
            throw new NCLAutoGenException("'%s': Cannot use both @CustomFormatter and @FormatTranslation on the same field".formatted(field.name()));
        } else if (formatter.isEmpty() && translation.isEmpty()) return;

        if (!(controller instanceof IValueFormattableController<?, ?>)) {
            throw new NCLAutoGenException("Attempted to use @CustomFormatter or @FormatTranslation on an option factory for field '%s' that uses a controller that does not support this.".formatted(field.name()));
        }

        IValueFormattableController<T, ?> typedBuilder = (IValueFormattableController<T, ?>) controller;

        formatter.ifPresent(formatterC -> {
            try {
                typedBuilder.formatValue((IValueFormatter<T>) formatterC.value().getConstructor().newInstance());
            } catch (Exception e) {
                throw new NCLAutoGenException("'%s': Failed to instantiate formatter class %s.".formatted(field.name(), formatterC.value().getName()), e);
            }
        });

        translation.ifPresent(translationC -> {
            typedBuilder.formatValue(v -> Component.translatable(translationC.value(), v));
        });
    }

    public static <T> T constructNoArgsClass(Class<T> clazz, Supplier<String> constructorNotFoundSupplier, Supplier<String> constructorFailedSupplier) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new NCLAutoGenException(constructorNotFoundSupplier.get(), e);
        } catch (Exception e) {
            throw new NCLAutoGenException(constructorFailedSupplier.get(), e);
        }
    }
}