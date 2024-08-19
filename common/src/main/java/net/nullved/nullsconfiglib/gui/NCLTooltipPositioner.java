package net.nullved.nullsconfiglib.gui;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.util.Mth;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.function.Supplier;

public class NCLTooltipPositioner implements ClientTooltipPositioner {
    private final Supplier<ScreenRectangle> buttonDimensions;

    public NCLTooltipPositioner(net.minecraft.client.gui.components.AbstractWidget widget) {
        this.buttonDimensions = widget::getRectangle;
    }

    public NCLTooltipPositioner(AbstractWidget widget) {
        this.buttonDimensions = () -> {
            Dimension<Integer> dim = widget.getDimension();
            return new ScreenRectangle(dim.x(), dim.y(), dim.width(), dim.height());
        };
    }

    public NCLTooltipPositioner(Supplier<ScreenRectangle> buttonDimensions) {
        this.buttonDimensions = buttonDimensions;
    }

    @Override
    public @NotNull Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        ScreenRectangle buttonDimensions = this.buttonDimensions.get();

        int centerX = buttonDimensions.left() + buttonDimensions.width() / 2;
        int aboveY = buttonDimensions.top() - tooltipHeight - 4;
        int belowY = buttonDimensions.top() + buttonDimensions.height() + 4;

        int maxBelow = screenHeight - (belowY + tooltipHeight);
        int minAbove = aboveY - tooltipHeight;

        int yResult = aboveY;
        if (minAbove < 8) yResult = maxBelow > minAbove ? belowY : aboveY;

        int xResult = Mth.clamp(centerX - tooltipWidth / 2, -4, screenWidth - tooltipWidth - 4);

        return new Vector2i(xResult, yResult);
    }
}
