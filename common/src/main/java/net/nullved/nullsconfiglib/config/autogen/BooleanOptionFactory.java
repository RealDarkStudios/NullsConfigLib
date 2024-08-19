package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.Boolean;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IBooleanControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;

public class BooleanOptionFactory extends SimpleOptionFactory<Boolean, java.lang.Boolean> {
    @Override
    protected IControllerBuilder<java.lang.Boolean> createController(Boolean annotation, IConfigField<java.lang.Boolean> field, IOptionAccess storage, IOption<java.lang.Boolean> option) {
        IBooleanControllerBuilder builder = IBooleanControllerBuilder.create(option).colored(annotation.colored());

        switch (annotation.formatter()) {
            case ON_OFF -> builder.onOffFormatter();
            case YES_NO -> builder.yesNoFormatter();
            case TRUE_FALSE -> builder.trueFalseFormatter();
            case CUSTOM -> builder.formatValue(v -> Component.translatable(getTranslationKey(field, "format." + v)));
        }

        return builder;
    }
}
