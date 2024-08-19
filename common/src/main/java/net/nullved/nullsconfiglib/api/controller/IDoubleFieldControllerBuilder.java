package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.DoubleFieldControllerBuilder;

public interface IDoubleFieldControllerBuilder extends INumberFieldControllerBuilder<Double, IDoubleFieldControllerBuilder> {
    static IDoubleFieldControllerBuilder create(IOption<Double> option) {
        return new DoubleFieldControllerBuilder(option);
    }
}
