package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.LongSlider;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.ILongSliderControllerBuilder;

public class LongSliderOptionFactory extends SimpleOptionFactory<LongSlider, Long> {

    @Override
    protected IControllerBuilder<Long> createController(LongSlider annotation, IConfigField<Long> field, IOptionAccess storage, IOption<Long> option) {
        return ILongSliderControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = getTranslationKey(field, "format." + v);
                    if (Language.getInstance().has(key)) return Component.translatable(key);

                    key = getTranslationKey(field, "format");

                    if (Language.getInstance().has(key)) return Component.translatable(key, v);
                    return Component.translatable(Long.toString(v));
                })
                .range(annotation.min(), annotation.max())
                .step(annotation.step());
    }
}
