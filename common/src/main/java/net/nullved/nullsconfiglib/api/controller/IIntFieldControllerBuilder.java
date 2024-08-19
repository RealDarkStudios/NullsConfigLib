package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.FloatFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.IntFieldControllerBuilder;

public interface IIntFieldControllerBuilder extends INumberFieldControllerBuilder<Integer, IIntFieldControllerBuilder> {
    static IIntFieldControllerBuilder create(IOption<Integer> option) {
        return new IntFieldControllerBuilder(option);
    }
}
