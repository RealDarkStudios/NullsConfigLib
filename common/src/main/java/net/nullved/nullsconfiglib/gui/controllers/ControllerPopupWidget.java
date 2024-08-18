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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public class ControllerPopupWidget<T extends IController<?>> extends ControllerWidget<IController<?>> implements GuiEventListener {
    public final ControllerWidget<?> entryWidget;
    public ControllerPopupWidget(T control, NCLScreen screen, Dimension<Integer> dim, ControllerWidget<?> entryWidget) {
        super(control, screen, dim);
        this.entryWidget = entryWidget;
    }

    public ControllerWidget<?> entryWidget() {
        return entryWidget;
    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return entryWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    public void close() {}

    public Component popupTitle() {
        return Component.translatable("ncl.control.text.blank");
    }

    @Override
    protected int getHoveredControlWidth() {
        return 0;
    }
}
