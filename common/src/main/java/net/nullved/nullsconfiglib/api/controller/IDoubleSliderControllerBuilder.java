package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.DoubleFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.DoubleSliderControllerBuilder;

public interface IDoubleSliderControllerBuilder extends ISliderControllerBuilder<Double, IDoubleSliderControllerBuilder> {
    static IDoubleSliderControllerBuilder create(IOption<Double> option) {
        return new DoubleSliderControllerBuilder(option);
    }
}
