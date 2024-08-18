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

package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.impl.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IOption<T> {
    @NotNull Component name();

    @NotNull IOptionDescription description();
    @NotNull Component tooltip();

    @NotNull IController<T> controller();

    @NotNull IBinding<T> binding();

    boolean available();

    void setAvailable(boolean available);

    @NotNull ImmutableSet<IOptionFlag> flags();

    boolean changed();

    @NotNull T pendingValue();

    void requestSet(@NotNull T value);

    boolean applyValue();

    void forgetPendingValue();

    void requestSetDefault();

    boolean isPendingValueDefault();

    default boolean canResetToDefault() {
        return true;
    }

    void addListener(BiConsumer<IOption<T>, T> listener);

    static <T> Builder<T> createBuilder() {
        return new Option.Builder<>();
    }

    interface Builder<T> {
        Builder<T> name(@NotNull Component name);
        Builder<T> description(@NotNull IOptionDescription description);
        Builder<T> description(@NotNull Function<T, IOptionDescription> descriptionFunction);
        Builder<T> controller(@NotNull Function<IOption<T>, IControllerBuilder<T>> controllerBuilder);
        Builder<T> customController(@NotNull Function<IOption<T>, IController<T>> control);
        Builder<T> binding(@NotNull IBinding<T> binding);
        Builder<T> binding(@NotNull T def, @NotNull Supplier<@NotNull T> getter, @NotNull Consumer<@NotNull T> setter);
        Builder<T> available(boolean available);
        Builder<T> flag(@NotNull IOptionFlag... flag);
        Builder<T> flags(@NotNull Collection<? extends IOptionFlag> flags);
        Builder<T> instant(boolean instant);
        Builder<T> listener(@NotNull BiConsumer<IOption<T>, T> listener);
        Builder<T> listeners(@NotNull Collection<BiConsumer<IOption<T>, T>> listeners);

        IOption<T> build();
    }
}
