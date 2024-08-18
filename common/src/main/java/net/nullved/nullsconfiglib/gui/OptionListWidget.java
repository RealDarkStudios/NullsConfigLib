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

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class OptionListWidget extends ElementListWidgetExt<OptionListWidget.Entry> {
    private final NCLScreen nclScreen;
    private final IConfigCategory category;
    private ImmutableList<Entry> viewableChildren;
    private String searchQuery = "";
    private final Consumer<OptionInfo> hoverEvent;
    private OptionInfo lastHoveredOption;

    public OptionListWidget(NCLScreen screen, IConfigCategory category, Minecraft client, int x, int y, int width, int height, Consumer<OptionInfo> hoverEvent) {
        super(client, x, y, width, height, true);
        this.nclScreen = screen;
        this.category = category;
        this.hoverEvent = hoverEvent;

        refreshOptions();

        for (IOptionGroup group : category.groups()) {
            if (group instanceof IListOption<?> listOption) {
                listOption.addRefreshListener(() -> refreshListEntries(listOption, category));
            }
        }
    }

    public void refreshOptions() {
        clearEntries();

        for (IOptionGroup group : category.groups()) {
            GroupSeparatorEntry groupSeparatorEntry;
            if (!group.isRoot()) {
                groupSeparatorEntry = group instanceof IListOption<?> listOption
                        ? new ListGroupSeparatorEntry(listOption, nclScreen)
                        : new GroupSeparatorEntry(group, nclScreen);
                addEntry(groupSeparatorEntry);
            } else {
                groupSeparatorEntry = null;
            }

            List<Entry> optionEntries = new ArrayList<>();

            // add empty entry to make sure users know it's empty not just bugging out
            if (groupSeparatorEntry instanceof ListGroupSeparatorEntry listGroupSeparatorEntry) {
                if (listGroupSeparatorEntry.listOption.options().isEmpty()) {
                    EmptyListLabel emptyListLabel = new EmptyListLabel(listGroupSeparatorEntry, category);
                    addEntry(emptyListLabel);
                    optionEntries.add(emptyListLabel);
                }
            }

            for (IOption<?> option : group.options()) {
                OptionEntry entry = new OptionEntry(option, category, group, groupSeparatorEntry, option.controller().provideWidget(nclScreen, getDefaultEntryDimension()));
                addEntry(entry);
                optionEntries.add(entry);
            }

            if (groupSeparatorEntry != null) {
                groupSeparatorEntry.setChildEntries(optionEntries);
            }
        }

        recacheViewableChildren();
        setScrollAmount(0);
        resetSmoothScrolling();
    }

    private void refreshListEntries(IListOption<?> listOption, IConfigCategory category) {
        // find group separator for group
        ListGroupSeparatorEntry groupSeparator = super.children().stream().filter(e -> e instanceof ListGroupSeparatorEntry gs && gs.group == listOption).map(ListGroupSeparatorEntry.class::cast).findAny().orElse(null);

        if (groupSeparator == null) {
            NullsConfigLib.LOGGER.warn("Can't find group seperator to refresh list option entries for list option " + listOption.name());
            return;
        }

        for (Entry entry : groupSeparator.childEntries)
            super.removeEntry(entry);
        groupSeparator.childEntries.clear();

        // if no entries, below loop won't run where addEntryBelow() recaches viewable children
        if (listOption.options().isEmpty()) {
            EmptyListLabel emptyListLabel;
            addEntryBelow(groupSeparator, emptyListLabel = new EmptyListLabel(groupSeparator, category));
            groupSeparator.childEntries.add(emptyListLabel);
            return;
        }

        Entry lastEntry = groupSeparator;
        for (IListOptionEntry<?> listOptionEntry : listOption.options()) {
            OptionEntry optionEntry = new OptionEntry(listOptionEntry, category, listOption, groupSeparator, listOptionEntry.controller().provideWidget(nclScreen, getDefaultEntryDimension()));
            addEntryBelow(lastEntry, optionEntry);
            groupSeparator.childEntries.add(optionEntry);
            lastEntry = optionEntry;
        }
    }

    public Dimension<Integer> getDefaultEntryDimension() {
        return Dimension.ofInt(getRowLeft(), 0, getRowWidth(), 20);
    }

    public void expandAllGroups() {
        for (Entry entry : super.children()) {
            if (entry instanceof GroupSeparatorEntry groupSeparatorEntry) {
                groupSeparatorEntry.setExpanded(true);
            }
        }
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft() - SCROLLBAR_WIDTH;
    }

    @Override
    public int getRowWidth() {
        return getWidth() - SCROLLBAR_WIDTH - 20; // 10 padding each side
    }

    public void updateSearchQuery(String query) {
        this.searchQuery = query;
        expandAllGroups();
        recacheViewableChildren();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Entry child : children()) {
            if (child != getEntryAtPosition(mouseX, mouseY) && child instanceof OptionEntry optionEntry)
                optionEntry.widget.unfocus();
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        super.mouseScrolled(mouseX, mouseY, horizontal, vertical);

        for (Entry child : children()) {
            if (child.mouseScrolled(mouseX, mouseY, horizontal, vertical))
                break;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.getFocused() != null && this.isDragging() && isValidMouseClick(button)) {
            return this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Entry child : children()) {
            if (child.keyPressed(keyCode, scanCode, modifiers))
                return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Entry child : children()) {
            if (child.charTyped(chr, modifiers))
                return true;
        }

        return super.charTyped(chr, modifiers);
    }

    public void recacheViewableChildren() {
        this.viewableChildren = ImmutableList.copyOf(super.children().stream().filter(Entry::isViewable).toList());

        // update y positions before they need to be rendered are rendered
        int i = 0;
        for (Entry entry : viewableChildren) {
            if (entry instanceof OptionEntry optionEntry)
                optionEntry.widget.setDimension(optionEntry.widget.getDimension().withY(getRowTop(i)));
            i++;
        }
    }

    @Override
    public List<Entry> children() {
        return viewableChildren;
    }

    public void addEntry(int index, Entry entry) {
        super.children().add(index, entry);
        recacheViewableChildren();
    }

    public void addEntryBelow(Entry below, Entry entry) {
        int idx = super.children().indexOf(below) + 1;

        if (idx == 0)
            throw new IllegalStateException("The entry to insert below does not exist!");

        addEntry(idx, entry);
    }

    public void addEntryBelowWithoutScroll(Entry below, Entry entry) {
        double d = (double)this.getMaxScroll() - this.getScrollAmount();
        addEntryBelow(below, entry);
        setScrollAmount(getMaxScroll() - d);
    }

    @Override
    public boolean removeEntryFromTop(Entry entry) {
        boolean ret = super.removeEntryFromTop(entry);
        recacheViewableChildren();
        return ret;
    }

    @Override
    public boolean removeEntry(Entry entry) {
        boolean ret = super.removeEntry(entry);
        recacheViewableChildren();
        return ret;
    }

    private void setHoverDescription(OptionInfo description) {
        if (description != lastHoveredOption) {
            lastHoveredOption = description;
            hoverEvent.accept(description);
        }
    }

    @Override
    protected void renderListBackground(GuiGraphics guiGraphics) {
    }

    public abstract class Entry extends ElementListWidgetExt.Entry<Entry> {
        public boolean isViewable() {
            return true;
        }

        protected boolean isHovered() {
            return Objects.equals(getHovered(), this);
        }
    }

    public class OptionEntry extends Entry {
        public final IOption<?> option;
        public final IConfigCategory category;
        public final IOptionGroup group;

        public final @Nullable GroupSeparatorEntry groupSeparatorEntry;

        public final AbstractWidget widget;

        private final TextScaledButtonWidget resetButton;

        private final String categoryName;
        private final String groupName;

        public OptionEntry(IOption<?> option, IConfigCategory category, IOptionGroup group, @Nullable GroupSeparatorEntry groupSeparatorEntry, AbstractWidget widget) {
            this.option = option;
            this.category = category;
            this.group = group;
            this.groupSeparatorEntry = groupSeparatorEntry;
            this.widget = widget;
            this.categoryName = category.name().getString().toLowerCase();
            this.groupName = group.name().getString().toLowerCase();
            if (option.canResetToDefault() && this.widget.canReset()) {
                this.widget.setDimension(this.widget.getDimension().expanded(-20, 0));
                this.resetButton = new TextScaledButtonWidget(nclScreen, widget.getDimension().xLimit(), -50, 20, 20, 2f, Component.literal("↻"), button -> {
                    option.requestSetDefault();
                });
                option.addListener((opt, val) -> this.resetButton.active = !opt.isPendingValueDefault() && opt.available());
                this.resetButton.active = !option.isPendingValueDefault() && option.available();
            } else {
                this.resetButton = null;
            }
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            widget.setDimension(widget.getDimension().withY(y));

            widget.render(graphics, mouseX, mouseY, tickDelta);

            if (resetButton != null) {
                resetButton.setY(y);
                resetButton.render(graphics, mouseX, mouseY, tickDelta);
            }

            if (isHovered()) {
                setHoverDescription(OptionInfo.of(option.name(), option.description(), option.flags()));
            }
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
            return widget.mouseScrolled(mouseX, mouseY, horizontal, vertical);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return widget.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            return widget.charTyped(chr, modifiers);
        }

        @Override
        public boolean isViewable() {
            return (groupSeparatorEntry == null || groupSeparatorEntry.isExpanded())
                    && (searchQuery.isEmpty()
                    || groupName.contains(searchQuery)
                    || widget.matchesSearch(searchQuery));
        }

        @Override
        public int getItemHeight() {
            return Math.max(widget.getDimension().height(), resetButton != null ? resetButton.getHeight() : 0) + 2;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (focused)
                setHoverDescription(OptionInfo.of(option.name(), option.description(), option.flags()));
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            if (resetButton == null)
                return ImmutableList.of(widget);

            return ImmutableList.of(widget, resetButton);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            if (resetButton == null)
                return ImmutableList.of(widget);

            return ImmutableList.of(widget, resetButton);
        }
    }

    public class GroupSeparatorEntry extends Entry {
        protected final IOptionGroup group;
        protected final MultiLineLabel wrappedName;
        protected final MultiLineLabel wrappedTooltip;

        protected final LowProfileButtonWidget expandMinimizeButton;

        protected final Screen screen;
        protected final Font font = Minecraft.getInstance().font;

        protected boolean groupExpanded;

        protected List<Entry> childEntries = new ArrayList<>();

        private int y;

        private GroupSeparatorEntry(IOptionGroup group, Screen screen) {
            this.group = group;
            this.screen = screen;
            this.wrappedName = MultiLineLabel.create(font, group.name(), getRowWidth() - 45);
            this.wrappedTooltip = MultiLineLabel.create(font, group.tooltip(), screen.width / 3 * 2 - 10);
            this.groupExpanded = !group.collapsed();
            this.expandMinimizeButton = new LowProfileButtonWidget(0, 0, 20, 20, Component.empty(), btn -> onExpandButtonPress());
            updateExpandMinimizeText();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.y = y;

            int buttonY = y + entryHeight / 2 - expandMinimizeButton.getHeight() / 2 + 1;

            expandMinimizeButton.setY(buttonY);
            expandMinimizeButton.setX(x);
            expandMinimizeButton.render(graphics, mouseX, mouseY, tickDelta);

            wrappedName.renderCentered(graphics, x + entryWidth / 2, y + getYPadding());

            if (isHovered()) {
                setHoverDescription(OptionInfo.of(group.name(), group.description(), Set.of()));
            }
        }

        public boolean isExpanded() {
            return groupExpanded;
        }

        public void setExpanded(boolean expanded) {
            if (this.groupExpanded == expanded)
                return;

            this.groupExpanded = expanded;
            updateExpandMinimizeText();
            recacheViewableChildren();
        }

        protected void onExpandButtonPress() {
            setExpanded(!isExpanded());
        }

        protected void updateExpandMinimizeText() {
            expandMinimizeButton.setMessage(Component.literal(isExpanded() ? "▼" : "▶"));
        }

        public void setChildEntries(List<? extends Entry> childEntries) {
            this.childEntries.clear();
            this.childEntries.addAll(childEntries);
        }

        @Override
        public boolean isViewable() {
            return searchQuery.isEmpty() || childEntries.stream().anyMatch(Entry::isViewable);
        }

        @Override
        public int getItemHeight() {
            return Math.max(wrappedName.getLineCount(), 1) * font.lineHeight + getYPadding() * 2;
        }

        private int getYPadding() {
            return 6;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            if (focused) setHoverDescription(OptionInfo.of(group.name(), group.description(), Set.of()));
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                @Override
                public @NotNull NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput builder) {
                    builder.add(NarratedElementType.TITLE, group.name());
                    builder.add(NarratedElementType.HINT, group.tooltip());
                }
            });
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(expandMinimizeButton);
        }
    }

    public class ListGroupSeparatorEntry extends GroupSeparatorEntry {
        private final IListOption<?> listOption;
        private final TextScaledButtonWidget resetListButton;
        private final TooltipButtonWidget addListButton;

        private ListGroupSeparatorEntry(IListOption<?> group, Screen screen) {
            super(group, screen);
            this.listOption = group;

            this.resetListButton = new TextScaledButtonWidget(screen, getRowRight() - 20, -50, 20, 20, 2f, Component.literal("\u21BB"), button -> {
                group.requestSetDefault();
            });
            group.addListener((opt, val) -> this.resetListButton.active = !opt.isPendingValueDefault() && opt.available());
            this.resetListButton.active = !group.isPendingValueDefault() && group.available();


            this.addListButton = new TooltipButtonWidget(nclScreen, resetListButton.getX() - 20, -50, 20, 20, Component.literal("+"), Component.translatable("ncl.list.add_top"), btn -> {
                group.insertNewEntry();
                setExpanded(true);
            });

            updateExpandMinimizeText();
            minimizeIfUnavailable();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            updateExpandMinimizeText(); // update every render because option could become available/unavailable at any time

            super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);

            int buttonY = expandMinimizeButton.getY();

            resetListButton.setY(buttonY);
            addListButton.setY(buttonY);

            resetListButton.render(graphics, mouseX, mouseY, tickDelta);
            addListButton.render(graphics, mouseX, mouseY, tickDelta);
        }

        private void minimizeIfUnavailable() {
            if (!listOption.available() && isExpanded()) {
                setExpanded(false);
            }
        }

        @Override
        protected void updateExpandMinimizeText() {
            super.updateExpandMinimizeText();
            expandMinimizeButton.active = listOption == null || listOption.available();
            if (addListButton != null)
                addListButton.active = expandMinimizeButton.active && listOption.numberOfEntries() < listOption.maxNumberOfEntries();
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(listOption.available() && expanded);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(expandMinimizeButton, addListButton, resetListButton);
        }
    }

    public class EmptyListLabel extends Entry {
        private final ListGroupSeparatorEntry parent;
        private final String groupName;
        private final String categoryName;

        public EmptyListLabel(ListGroupSeparatorEntry parent, IConfigCategory category) {
            this.parent = parent;
            this.groupName = parent.group.name().getString().toLowerCase();
            this.categoryName = category.name().getString().toLowerCase();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            graphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("ncl.list.empty").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC), x + entryWidth / 2, y, -1);
        }

        @Override
        public boolean isViewable() {
            return parent.isExpanded() && (searchQuery.isEmpty() || groupName.contains(searchQuery));
        }

        @Override
        public int getItemHeight() {
            return 11;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of();
        }
    }
}
