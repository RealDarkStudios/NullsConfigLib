package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.config.autogen.TickBox;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.ITickBoxControllerBuilder;

public class TickBoxOptionFactory extends SimpleOptionFactory<TickBox, Boolean> {
    @Override
    protected IControllerBuilder<Boolean> createController(TickBox annotation, IConfigField<Boolean> field, IOptionAccess storage, IOption<Boolean> option) {
        return ITickBoxControllerBuilder.create(option);
    }
}
