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

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.impl.NCLGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface INCLGui {
    Component title();
    ImmutableList<IConfigCategory> categories();
    Runnable saveFunction();
    Consumer<NCLScreen> initConsumer();
    Screen generateScreen(@Nullable Screen parent);
    static Builder createBuilder() {
        return new NCLGui.Builder();
    }

    static <T> INCLGui create(IConfigHandler<T> configHandler, ConfigBackedBuilder<T> builder) {
        return builder.build(configHandler.defaults(), configHandler.instance(), createBuilder().save(configHandler::save)).build();
    }

    interface Builder {
        Builder title(@NotNull Component title);
        Builder category(@NotNull IConfigCategory category);
        Builder categories(@NotNull Collection<? extends IConfigCategory> categories);
        Builder save(@NotNull Runnable saveFunction);
        Builder screenInit(@NotNull Consumer<NCLScreen> initConsumer);
        INCLGui build();
    }

    @FunctionalInterface
    interface ConfigBackedBuilder<T> {
        INCLGui.Builder build(T defaults, T config, INCLGui.Builder builder);
    }
}
