/*
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
