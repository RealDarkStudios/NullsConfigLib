package net.nullved.nullsconfiglib.gui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class NCLTooltip extends Tooltip {
    private final AbstractWidget widget;

    public NCLTooltip(Component tooltip, AbstractWidget widget) {
        super(tooltip, tooltip);
        this.widget = widget;
    }
}
