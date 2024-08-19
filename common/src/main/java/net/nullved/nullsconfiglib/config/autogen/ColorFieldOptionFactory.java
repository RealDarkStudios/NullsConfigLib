package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.ColorField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IColorControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;

import java.awt.*;

public class ColorFieldOptionFactory extends SimpleOptionFactory<ColorField, Color> {
    @Override
    protected IControllerBuilder<Color> createController(ColorField annotation, IConfigField<Color> field, IOptionAccess storage, IOption<Color> option) {
        return IColorControllerBuilder.create(option).allowAlpha(annotation.allowAlpha());
    }
}
