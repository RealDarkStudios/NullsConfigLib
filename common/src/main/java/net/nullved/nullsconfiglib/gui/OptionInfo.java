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

package net.nullved.nullsconfiglib.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOptionDescription;
import net.nullved.nullsconfiglib.api.IOptionFlag;

import java.util.Set;

public record OptionInfo(Component name, IOptionDescription description, Set<IOptionFlag> flags) {
    public static OptionInfo of(Component name, IOptionDescription description, Set<IOptionFlag> flags) {
        return new OptionInfo(name.copy().withStyle(ChatFormatting.BOLD), description, flags);
    }
}