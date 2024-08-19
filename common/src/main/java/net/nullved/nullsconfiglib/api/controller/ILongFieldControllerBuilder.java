package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.DoubleFieldControllerBuilder;
import net.nullved.nullsconfiglib.impl.controller.LongFieldControllerBuilder;

public interface ILongFieldControllerBuilder extends INumberFieldControllerBuilder<Long, ILongFieldControllerBuilder> {
    static ILongFieldControllerBuilder create(IOption<Long> option) {
        return new LongFieldControllerBuilder(option);
    }
}
