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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RequireRestartScreen extends ConfirmScreen {
    public RequireRestartScreen(Screen parent) {
        super(option -> {
            if (option) Minecraft.getInstance().stop();
            else Minecraft.getInstance().setScreen(parent);
        },
            Component.translatable("ncl.restart.title").withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
            Component.translatable("ncl.restart.message"),
            Component.translatable("ncl.restart.yes"),
            Component.translatable("ncl.restart.no")
        );
    }
}
