package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.StringControllerBuilder;

public interface IStringControllerBuilder extends IControllerBuilder<String> {
    static IStringControllerBuilder create(IOption<String> option) {
        return new StringControllerBuilder(option);
    }
}
