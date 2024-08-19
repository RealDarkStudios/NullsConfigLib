/*
 */

package net.nullved.nullsconfiglib.gui.controllers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public class PopupControllerScreen extends Screen {
    private final NCLScreen backgroundNCLScreen;
    private final ControllerPopupWidget<?> controllerPopup;

    public PopupControllerScreen(NCLScreen backgroundNCLScreen, ControllerPopupWidget<?> controllerPopup) {
        super(controllerPopup.popupTitle());
        this.backgroundNCLScreen = backgroundNCLScreen;
        this.controllerPopup = controllerPopup;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(this.controllerPopup);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        minecraft.setScreen(backgroundNCLScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        controllerPopup.render(guiGraphics, mouseX, mouseY, partialTick);
        this.backgroundNCLScreen.render(guiGraphics, -1, -1, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (controllerPopup.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        }
        backgroundNCLScreen.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        controllerPopup.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return controllerPopup.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return controllerPopup.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.minecraft.screen = backgroundNCLScreen;
    }
}
