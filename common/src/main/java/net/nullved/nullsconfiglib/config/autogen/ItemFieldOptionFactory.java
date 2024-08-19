package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.world.item.Item;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.ItemField;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IItemControllerBuilder;

public class ItemFieldOptionFactory extends SimpleOptionFactory<ItemField, Item> {
    @Override
    protected IControllerBuilder<Item> createController(ItemField annotation, IConfigField<Item> field, IOptionAccess storage, IOption<Item> option) {
        return IItemControllerBuilder.create(option);
    }
}
