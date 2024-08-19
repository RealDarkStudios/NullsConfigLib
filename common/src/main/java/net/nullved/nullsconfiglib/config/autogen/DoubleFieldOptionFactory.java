package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.DoubleField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IDoubleFieldControllerBuilder;

public class DoubleFieldOptionFactory extends SimpleOptionFactory<DoubleField, Double> {

    @Override
    protected IControllerBuilder<Double> createController(DoubleField annotation, IConfigField<Double> field, IOptionAccess storage, IOption<Double> option) {
        return IDoubleFieldControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = null;
                    if (v == annotation.min()) key = getTranslationKey(field, "format.min");
                    else if (v == annotation.max()) key = getTranslationKey(field, "format.max");

                    if (key != null && Language.getInstance().has(key)) return Component.translatable(key);
                    return Component.translatable(String.format(annotation.format(), v));
                })
                .range(annotation.min(), annotation.max());
    }
}
