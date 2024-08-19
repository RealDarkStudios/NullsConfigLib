package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.MasterTickBox;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.ITickBoxControllerBuilder;

public class MasterTickBoxOptionFactory extends SimpleOptionFactory<MasterTickBox, Boolean> {
    @Override
    protected IControllerBuilder<Boolean> createController(MasterTickBox annotation, IConfigField<Boolean> field, IOptionAccess storage, IOption<Boolean> option) {
        return ITickBoxControllerBuilder.create(option);
    }

    @Override
    protected void listener(MasterTickBox annotation, IConfigField<Boolean> field, IOptionAccess storage, IOption<Boolean> option, Boolean value) {
        for (String child: annotation.fields()) {
            storage.scheduleOptionOperation(child, childOpt -> {
                childOpt.setAvailable(annotation.invert() != value);
            });
        }
    }
}
