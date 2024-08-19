package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.TickBoxControllerBuilder;

public interface ITickBoxControllerBuilder extends IControllerBuilder<Boolean> {
    static ITickBoxControllerBuilder create(IOption<Boolean> option) {
        return new TickBoxControllerBuilder(option);
    }
}
