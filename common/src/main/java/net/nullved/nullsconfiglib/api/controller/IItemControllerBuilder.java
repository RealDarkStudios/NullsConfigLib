package net.nullved.nullsconfiglib.api.controller;

import net.minecraft.world.item.Item;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.ItemControllerBuilder;

public interface IItemControllerBuilder extends IControllerBuilder<Item> {
    static IItemControllerBuilder create(IOption<Item> option) {
        return new ItemControllerBuilder(option);
    }
}
