package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.ColorControllerBuilder;

import java.awt.*;

public interface IColorControllerBuilder extends IControllerBuilder<Color> {
    IColorControllerBuilder allowAlpha(boolean allowAlpha);

    static IColorControllerBuilder create(IOption<Color> option) {
        return new ColorControllerBuilder(option);
    }
}
