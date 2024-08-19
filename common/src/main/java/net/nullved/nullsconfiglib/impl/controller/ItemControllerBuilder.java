package net.nullved.nullsconfiglib.impl.controller;

import net.minecraft.world.item.Item;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IItemControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.dropdown.ItemController;

public class ItemControllerBuilder extends AbstractControllerBuilder<Item> implements IItemControllerBuilder {
    public ItemControllerBuilder(IOption<Item> option) {
        super(option);
    }

    @Override
    public IController<Item> build() {
        return new ItemController(option);
    }
}
