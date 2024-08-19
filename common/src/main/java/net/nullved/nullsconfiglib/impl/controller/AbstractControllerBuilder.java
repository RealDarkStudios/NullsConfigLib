package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;

public abstract class AbstractControllerBuilder<T> implements IControllerBuilder<T> {
    protected final IOption<T> option;

    protected AbstractControllerBuilder(IOption<T> option) {
        this.option = option;
    }
}
