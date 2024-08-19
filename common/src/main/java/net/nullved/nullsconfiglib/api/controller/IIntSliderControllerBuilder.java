package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.IntFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.IntSliderControllerBuilder;

public interface IIntSliderControllerBuilder extends ISliderControllerBuilder<Integer, IIntSliderControllerBuilder> {
    static IIntSliderControllerBuilder create(IOption<Integer> option) {
        return new IntSliderControllerBuilder(option);
    }
}
