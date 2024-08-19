package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.LongFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.LongSliderControllerBuilder;

public interface ILongSliderControllerBuilder extends ISliderControllerBuilder<Long, ILongSliderControllerBuilder> {
    static ILongSliderControllerBuilder create(IOption<Long> option) {
        return new LongSliderControllerBuilder(option);
    }
}
