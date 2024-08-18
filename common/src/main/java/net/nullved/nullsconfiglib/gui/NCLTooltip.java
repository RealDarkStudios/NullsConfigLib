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
