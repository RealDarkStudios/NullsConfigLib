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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TextScaledButtonWidget extends TooltipButtonWidget {
    private final float textScale;

    public TextScaledButtonWidget(Screen screen, int x, int y, int width, int height, float textScale, Component message, Component tooltip, OnPress onPress) {
        super(screen, x, y, width, height, message, tooltip, onPress);
        this.textScale = textScale;
    }

    public TextScaledButtonWidget(Screen screen, int x, int y, int width, int height, float textScale, Component message, OnPress onPress) {
        this(screen, x, y, width, height, textScale, message, null, onPress);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font textRenderer, int color) {
        Font font = Minecraft.getInstance().font;
        PoseStack pose = guiGraphics.pose();

        pose.pushPose();
        pose.translate(((this.getX() + this.width / 2f) - font.width(getMessage()) * textScale / 2), (float) this.getY() + (this.height - 8 * textScale) / 2f / textScale, 0);
        pose.scale(textScale, textScale, 1);
        guiGraphics.drawString(font, getMessage(), 0, 0, color | Mth.ceil(this.alpha * 255.0F) << 24, true);
        pose.popPose();
    }
}
