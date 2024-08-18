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

package net.nullved.nullsconfiglib.gui.controllers.dropdown;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.utils.ItemRegistryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemControllerElement extends AbstractDropdownControllerElement<Item, ResourceLocation> {
    private final ItemController itemController;
    protected Item currentItem = null;
    protected Map<ResourceLocation, Item> matchingItems = new HashMap<>();


    public ItemControllerElement(ItemController control, NCLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        this.itemController = control;
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var oldDimension = getDimension();
        setDimension(getDimension().withWidth(getDimension().width() - getDecorationPadding()));
        super.drawValueText(graphics, mouseX, mouseY, delta);
        setDimension(oldDimension);
        if (currentItem != null) {
            graphics.renderFakeItem(new ItemStack(currentItem), getDimension().xLimit() - getXPadding() - getDecorationPadding() + 2, getDimension().y() + 2);
        }
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> identifiers = ItemRegistryHelper.getMatchingItemIdentifiers(inputField).toList();
        currentItem = ItemRegistryHelper.getItemFromName(inputField, null);
        for (ResourceLocation identifier : identifiers) {
            matchingItems.put(identifier, BuiltInRegistries.ITEM.get(identifier));
        }
        return identifiers;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, Dimension<Integer> entryDimension, ResourceLocation identifier) {
        super.renderDropdownEntry(graphics, entryDimension, identifier);
        graphics.renderFakeItem(
                new ItemStack(matchingItems.get(identifier)),
                entryDimension.xLimit() - 2,
                entryDimension.y() + 1
        );
    }

    @Override
    public String getString(ResourceLocation identifier) {
        return identifier.toString();
    }

    @Override
    protected int getDecorationPadding() {
        return 16;
    }

    @Override
    protected int getDropdownEntryPadding() {
        return 4;
    }

    @Override
    protected int getControlWidth() {
        return super.getControlWidth() + getDecorationPadding();
    }

    @Override
    protected Component getValueText() {
        if (inputField.isEmpty() || itemController == null)
            return super.getValueText();

        if (inputFieldFocused)
            return Component.literal(inputField);

        return itemController.option().pendingValue().getDescription();
    }
}
