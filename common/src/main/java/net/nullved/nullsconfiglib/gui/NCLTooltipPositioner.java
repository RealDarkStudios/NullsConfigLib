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
