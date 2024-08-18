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

package net.nullved.nullsconfiglib.gui.controllers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.tab.TabExt;

public class TickBoxController implements IController<Boolean> {
    private final IOption<Boolean> option;

    public TickBoxController(IOption<Boolean> option) {
        this.option = option;
    }

    @Override
    public IOption<Boolean> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return Component.empty();
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new TickBoxControllerElement(this, screen, widgetDimension);
    }

    public static class TickBoxControllerElement extends ControllerWidget<TickBoxController> {
        public TickBoxControllerElement(TickBoxController controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
            super(controller, screen, widgetDimension);
        }

        @Override
        protected void drawHoveredControl(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            int outlineSize = 10;
            int outlineX1 = getDimension().xLimit() - getXPadding() - outlineSize;
            int outlineY1 = getDimension().centerY() - outlineSize / 2;
            int outlineX2 = getDimension().xLimit() - getXPadding();
            int outlineY2 = getDimension().centerY() + outlineSize / 2;

            int color = getValueColor();
            int shadowColor = multiplyColor(color, 0.25f);

            drawOutline(graphics, outlineX1 + 1, outlineY1 + 1, outlineX2 + 1, outlineY2 + 1, 1, shadowColor);
            drawOutline(graphics, outlineX1, outlineY1, outlineX2, outlineY2, 1, color);
            if (controller.option().pendingValue()) {
                graphics.fill(outlineX1 + 3, outlineY1 + 3, outlineX2 - 1, outlineY2 - 1, shadowColor);
                graphics.fill(outlineX1 + 2, outlineY1 + 2, outlineX2 - 2, outlineY2 - 2, color);
            }
        }
        @Override
        protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            if (!isHovered())
                drawHoveredControl(graphics, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY) || !isAvailable())
                return false;

            toggleSetting();
            return true;
        }

        @Override
        protected int getHoveredControlWidth() {
            return 10;
        }

        @Override
        protected int getUnhoveredControlWidth() {
            return 10;
        }

        public void toggleSetting() {
            controller.option().requestSet(!controller.option().pendingValue());
            playDownSound();
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!focused) {
                return false;
            }

            if (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_SPACE || keyCode == InputConstants.KEY_NUMPADENTER) {
                toggleSetting();
                return true;
            }

            return false;
        }
    }
}
