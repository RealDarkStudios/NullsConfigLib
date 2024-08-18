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

package net.nullved.nullsconfiglib.gui.controllers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.utils.GuiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ControllerWidget<T extends IController<?>> extends AbstractWidget {
    protected final T controller;
    protected MultiLineLabel wrappedTooltip;
    protected final NCLScreen screen;

    protected boolean focused = false, hovered = false;

    protected final Component modifiedOptionName;
    protected final String optionNameString;

    public ControllerWidget(T controller, NCLScreen screen, Dimension<Integer> dim) {
        super(dim);
        this.controller = controller;
        this.screen = screen;
        controller.option().addListener((opt, pending) -> updateTooltip());
        updateTooltip();
        this.modifiedOptionName = controller.option().name().copy().withStyle(ChatFormatting.ITALIC);
        this.optionNameString = controller.option().name().getString().toLowerCase();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        hovered = isMouseOver(mouseX, mouseY);

        Component name = controller.option().changed() ? modifiedOptionName : controller.option().name();
        Component shortenedName = Component.literal(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

        drawButtonRect(graphics, getDimension().x(), getDimension().y(), getDimension().xLimit(), getDimension().yLimit(), hovered || focused, isAvailable());
        graphics.drawString(textRenderer, shortenedName, getDimension().x() + getXPadding(), getTextY(), getValueColor(), true);

        drawValueText(graphics, mouseX, mouseY, delta);
        if (isHovered()) {
            drawHoveredControl(graphics, mouseX, mouseY, delta);
        }
    }

    protected void drawHoveredControl(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        Component valueText = getValueText();
        graphics.drawString(textRenderer, valueText, getDimension().xLimit() - textRenderer.width(valueText) - getXPadding(), getTextY(), getValueColor(), true);
    }

    private void updateTooltip() {
        this.wrappedTooltip = MultiLineLabel.create(textRenderer, controller.option().tooltip(), screen.width / 3 * 2 - 10);
    }

    protected int getControlWidth() {
        return isHovered() ? getHoveredControlWidth() : getUnhoveredControlWidth();
    }

    public boolean isHovered() {
        return isAvailable() && (hovered || focused);
    }

    protected abstract int getHoveredControlWidth();

    protected int getUnhoveredControlWidth() {
        return textRenderer.width(getValueText());
    }

    protected int getXPadding() {
        return 5;
    }

    protected int getYPadding() {
        return 2;
    }

    protected Component getValueText() {
        return controller.formatValue();
    }

    protected boolean isAvailable() {
        return controller.option().available();
    }

    protected int getValueColor() {
        return isAvailable() ? -1 : inactiveColor;
    }

    @Override
    public boolean canReset() {
        return true;
    }

    protected int getTextY() {
        return (int)(getDimension().y() + getDimension().height() / 2f - textRenderer.lineHeight / 2f);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        return !this.isFocused() ? ComponentPath.leaf(this) : null;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void unfocus() {
        this.focused = false;
    }

    @Override
    public boolean matchesSearch(String query) {
        return optionNameString.contains(query.toLowerCase());
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return focused ? NarrationPriority.FOCUSED : isHovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, controller.option().name());
        builder.add(NarratedElementType.HINT, controller.option().tooltip());
    }
}
