package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.IntField;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IIntFieldControllerBuilder;

public class IntFieldOptionFactory extends SimpleOptionFactory<IntField, Integer> {

    @Override
    protected IControllerBuilder<Integer> createController(IntField annotation, IConfigField<Integer> field, IOptionAccess storage, IOption<Integer> option) {
        return IIntFieldControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = getTranslationKey(field, "format." + v);
                    if (Language.getInstance().has(key)) return Component.translatable(key);

                    key = getTranslationKey(field, "format");

                    if (Language.getInstance().has(key)) return Component.translatable(key, v);
                    return Component.translatable(Integer.toString(v));
                })
                .range(annotation.min(), annotation.max());
    }
}
