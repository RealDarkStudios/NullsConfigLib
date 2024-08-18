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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.nullved.nullsconfiglib.gui.utils.NCLRenderHelper;

public class TooltipButtonWidget extends Button {
    protected final Screen screen;

    public TooltipButtonWidget(Screen screen, int x, int y, int width, int height, Component message, Component tooltip, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.screen = screen;
        if (tooltip != null) setTooltip(new NCLTooltip(tooltip, this));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        NCLRenderHelper.renderButtonTexture(guiGraphics, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.active, this.isHoveredOrFocused());

        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
