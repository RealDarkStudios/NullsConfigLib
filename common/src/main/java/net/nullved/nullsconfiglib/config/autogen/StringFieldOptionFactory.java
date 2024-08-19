package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.config.autogen.StringField;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IStringControllerBuilder;

public class StringFieldOptionFactory extends SimpleOptionFactory<StringField, String> {
    @Override
    protected IControllerBuilder<String> createController(StringField annotation, IConfigField<String> field, IOptionAccess storage, IOption<String> option) {
        return IStringControllerBuilder.create(option);
    }
}
