package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.FloatSlider;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IFloatSliderControllerBuilder;

public class FloatSliderOptionFactory extends SimpleOptionFactory<FloatSlider, Float> {

    @Override
    protected IControllerBuilder<Float> createController(FloatSlider annotation, IConfigField<Float> field, IOptionAccess storage, IOption<Float> option) {
        return IFloatSliderControllerBuilder.create(option)
                .formatValue(v -> {
                    String key = null;
                    if (v == annotation.min()) key = getTranslationKey(field, "format.min");
                    else if (v == annotation.max()) key = getTranslationKey(field, "format.max");
                    
                    if (key != null && Language.getInstance().has(key)) return Component.translatable(key);
                    return Component.translatable(String.format(annotation.format(), v));
                })
                .range(annotation.min(), annotation.max())
                .step(annotation.step());
    }
}
