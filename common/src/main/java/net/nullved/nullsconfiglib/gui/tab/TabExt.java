package net.nullved.nullsconfiglib.gui.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.Tab;
import org.jetbrains.annotations.NotNull;

public interface TabExt extends Tab {
    @NotNull Tooltip getTooltip();

    default void tick() {}

    default void renderBackground(GuiGraphics graphics) {}
}
