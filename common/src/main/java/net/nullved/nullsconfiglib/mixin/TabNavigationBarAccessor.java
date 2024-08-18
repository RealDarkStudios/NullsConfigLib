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

package net.nullved.nullsconfiglib.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TabNavigationBar.class)
public interface TabNavigationBarAccessor {
    @Accessor("layout")
    LinearLayout ncl$getLayout();

    @Accessor("width")
    int ncl$getWidth();

    @Accessor("tabManager")
    TabManager ncl$getTabManager();

    @Accessor("tabs")
    ImmutableList<Tab> ncl$getTabs();

    @Accessor("tabButtons")
    ImmutableList<TabButton> ncl$getTabButtons();
}
