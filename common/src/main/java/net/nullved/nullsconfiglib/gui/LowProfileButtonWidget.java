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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class LowProfileButtonWidget extends Button {
    protected LowProfileButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    public LowProfileButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, Tooltip tooltip) {
        this(x, y, width, height, message, onPress);
        setTooltip(tooltip);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isHoveredOrFocused() || !isActive()) {
            int j = this.active ? 0xFFFFFF : 0xA0A0A0;
            this.renderString(guiGraphics, Minecraft.getInstance().font, j);
        } else {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
