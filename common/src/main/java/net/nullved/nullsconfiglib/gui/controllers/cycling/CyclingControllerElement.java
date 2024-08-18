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

package net.nullved.nullsconfiglib.gui.controllers.cycling;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.controllers.ControllerWidget;

public class CyclingControllerElement extends ControllerWidget<ICyclingController<?>> {
    public CyclingControllerElement(ICyclingController<?> controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
        super(controller, screen, widgetDimension);
    }

    public void cycleValue(int increment) {
        int targetIdx = controller.getPendingValue() + increment;
        if (targetIdx >= controller.getCycleLength()) {
            targetIdx -= controller.getCycleLength();
        } else if (targetIdx < 0) {
            targetIdx += controller.getCycleLength();
        }

        controller.setPendingValue(targetIdx);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY) || (button != InputConstants.MOUSE_BUTTON_LEFT && button != InputConstants.MOUSE_BUTTON_RIGHT) || !isAvailable()) {
            return false;
        }

        playDownSound();
        cycleValue(button == InputConstants.MOUSE_BUTTON_RIGHT || Screen.hasShiftDown() || Screen.hasControlDown() ? -1 : 1);

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused)
            return false;

        switch (keyCode) {
            case InputConstants.KEY_LEFT ->
                    cycleValue(-1);
            case InputConstants.KEY_RIGHT ->
                    cycleValue(1);
            case InputConstants.KEY_RETURN, InputConstants.KEY_SPACE, InputConstants.KEY_NUMPADENTER ->
                    cycleValue(Screen.hasControlDown() || Screen.hasShiftDown() ? -1 : 1);
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    protected int getHoveredControlWidth() {
        return getUnhoveredControlWidth();
    }
}
