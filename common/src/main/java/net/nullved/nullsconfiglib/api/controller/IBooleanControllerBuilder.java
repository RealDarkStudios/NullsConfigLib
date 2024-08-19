package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.BooleanControllerBuilder;

public interface IBooleanControllerBuilder extends IValueFormattableController<Boolean, IBooleanControllerBuilder> {
    IBooleanControllerBuilder colored(boolean colored);

    IBooleanControllerBuilder onOffFormatter();
    IBooleanControllerBuilder yesNoFormatter();
    IBooleanControllerBuilder trueFalseFormatter();

    static IBooleanControllerBuilder create(IOption<Boolean> option) {
        return new BooleanControllerBuilder(option);
    }
}
