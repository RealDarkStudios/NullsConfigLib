package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.FloatFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.LongFieldControllerBuilder;

public interface IFloatFieldControllerBuilder extends INumberFieldControllerBuilder<Float, IFloatFieldControllerBuilder> {
    static IFloatFieldControllerBuilder create(IOption<Float> option) {
        return new FloatFieldControllerBuilder(option);
    }
}
