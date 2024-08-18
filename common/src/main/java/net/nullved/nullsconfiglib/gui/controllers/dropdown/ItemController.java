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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.utils.ItemRegistryHelper;

public class ItemController extends AbstractDropdownController<Item> {
    public ItemController(IOption<Item> option) {
        super(option);
    }

    @Override
    public String getString() {
        return BuiltInRegistries.ITEM.getKey(option.pendingValue()).toString();
    }

    @Override
    public void setFromString(String value) {
        option.requestSet(ItemRegistryHelper.getItemFromName(value, option.pendingValue()));
    }

    @Override
    public Component formatValue() {
        return Component.literal(getString());
    }


    @Override
    public boolean isValueValid(String value) {
        return ItemRegistryHelper.isRegisteredItem(value);
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return ItemRegistryHelper.getMatchingItemIdentifiers(value)
                .skip(offset)
                .findFirst()
                .map(ResourceLocation::toString)
                .orElseGet(this::getString);
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new ItemControllerElement(this, screen, widgetDimension);
    }
}
