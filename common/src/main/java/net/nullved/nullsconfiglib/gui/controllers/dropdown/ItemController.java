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
