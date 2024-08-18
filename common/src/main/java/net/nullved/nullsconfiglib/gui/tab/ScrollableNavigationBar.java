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

package net.nullved.nullsconfiglib.gui.tab;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.util.Mth;
import net.nullved.nullsconfiglib.mixin.TabNavigationBarAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScrollableNavigationBar extends TabNavigationBar {
    private static final int NAVBAR_MARGIN = 28;
    private static final Font font = Minecraft.getInstance().font;

    private int scrollOffset, maxScrollOffset;

    private final TabNavigationBarAccessor accessor;

    public ScrollableNavigationBar(int width, TabManager tabManager, Iterable<? extends Tab> tabs) {
        super(width, tabManager, ImmutableList.copyOf(tabs));
        this.accessor = (TabNavigationBarAccessor) this;

        for (TabButton tabButton: accessor.ncl$getTabButtons()) {
            if (tabButton.tab() instanceof TabExt tab) {
                tabButton.setTooltip(tab.getTooltip());
            }
        }
    }

    @Override
    public void arrangeElements() {
        ImmutableList<TabButton> tabButtons = accessor.ncl$getTabButtons();
        int noScrollWidth = accessor.ncl$getWidth() - NAVBAR_MARGIN * 2;

        int allTabsWidth = 0;

        for (TabButton tabButton: tabButtons) {
            int buttonWidth = font.width(tabButton.getMessage()) + 20;
            allTabsWidth += buttonWidth;
            tabButton.setWidth(buttonWidth);
        }

        if (allTabsWidth < noScrollWidth) {
            int equalWidth = noScrollWidth / tabButtons.size();
            List<TabButton> smallTabs = tabButtons.stream().filter(btn -> btn.getWidth() < equalWidth).toList();
            List<TabButton> bigTabs = tabButtons.stream().filter(btn -> btn.getWidth() >= equalWidth).toList();
            int leftoverWidth = noScrollWidth - bigTabs.stream().mapToInt(TabButton::getWidth).sum();
            int equalWidthForSmallTabs = leftoverWidth / smallTabs.size();
            for (TabButton tabButton: smallTabs) {
                tabButton.setWidth(equalWidthForSmallTabs);
            }

            allTabsWidth = noScrollWidth;
        }

        Layout layout = accessor.ncl$getLayout();
        layout.arrangeElements();
        layout.setY(0);
        scrollOffset = 0;

        layout.setX(Math.max((accessor.ncl$getWidth() - allTabsWidth) / 2, NAVBAR_MARGIN));
        this.maxScrollOffset = Math.max(0, allTabsWidth - noScrollWidth);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(0, 0, 10);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.setScrollOffset(this.scrollOffset - (int)(scrollY * 15));
        return true;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseY <= 24;
    }

    public void setScrollOffset(int scrollOffset) {
        Layout layout = accessor.ncl$getLayout();

        layout.setX(layout.getX() + this.scrollOffset);
        this.scrollOffset = Mth.clamp(scrollOffset, 0, maxScrollOffset);
        layout.setX(layout.getX() - this.scrollOffset);
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener child) {
        super.setFocused(child);
        if (child instanceof TabButton tabButton) {
            this.ensureVisible(tabButton);
        }
    }

    protected void ensureVisible(TabButton tabButton) {
        if (tabButton.getX() < NAVBAR_MARGIN) {
            this.setScrollOffset(this.scrollOffset - (NAVBAR_MARGIN - tabButton.getX()));
        } else if (tabButton.getX() + tabButton.getWidth() > accessor.ncl$getWidth() - NAVBAR_MARGIN) {
            this.setScrollOffset(this.scrollOffset + (tabButton.getX() + tabButton.getWidth() - (accessor.ncl$getWidth() - NAVBAR_MARGIN)));
        }
    }

    public ImmutableList<Tab> getTabs() {
        return accessor.ncl$getTabs();
    }

    public TabManager getTabManager() {
        return accessor.ncl$getTabManager();
    }
}
