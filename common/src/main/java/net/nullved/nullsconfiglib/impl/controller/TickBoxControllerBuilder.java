package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.ITickBoxControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.TickBoxController;

public class TickBoxControllerBuilder extends AbstractControllerBuilder<Boolean> implements ITickBoxControllerBuilder {
    public TickBoxControllerBuilder(IOption<Boolean> option) {
        super(option);
    }

    @Override
    public IController<Boolean> build() {
        return new TickBoxController(option);
    }
}
