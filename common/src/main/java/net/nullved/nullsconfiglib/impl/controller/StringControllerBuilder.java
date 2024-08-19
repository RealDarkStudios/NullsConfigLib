package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IStringControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.string.StringController;

public class StringControllerBuilder extends AbstractControllerBuilder<String> implements IStringControllerBuilder {
    public StringControllerBuilder(IOption<String> option) {
        super(option);
    }

    @Override
    public IController<String> build() {
        return new StringController(option);
    }
}
