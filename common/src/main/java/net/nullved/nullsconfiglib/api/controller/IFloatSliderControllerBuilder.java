package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.FloatFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.FloatSliderControllerBuilder;

public interface IFloatSliderControllerBuilder extends ISliderControllerBuilder<Float, IFloatSliderControllerBuilder> {
    static IFloatSliderControllerBuilder create(IOption<Float> option) {
        return new FloatSliderControllerBuilder(option);
    }
}
