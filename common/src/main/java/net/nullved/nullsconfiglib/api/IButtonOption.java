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

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.impl.ButtonOption;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface IButtonOption extends IOption<BiConsumer<NCLScreen, IButtonOption>> {
    BiConsumer<NCLScreen, IButtonOption> action();

    static Builder createBuilder() {
        return new ButtonOption.Builder();
    }

    interface Builder {
        Builder name(@NotNull Component name);
        Builder text(@NotNull Component text);
        Builder description(@NotNull IOptionDescription description);
        Builder action(@NotNull BiConsumer<NCLScreen, IButtonOption> action);
        Builder available(boolean available);

        IButtonOption build();
    }
}
