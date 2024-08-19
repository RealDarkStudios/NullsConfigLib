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
