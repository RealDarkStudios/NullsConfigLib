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
