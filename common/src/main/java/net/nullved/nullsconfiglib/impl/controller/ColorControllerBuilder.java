package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IColorControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.ColorController;

import java.awt.Color;

public class ColorControllerBuilder extends AbstractControllerBuilder<Color> implements IColorControllerBuilder {
    private boolean allowAlpha = false;

    public ColorControllerBuilder(IOption<Color> option) {
        super(option);
    }

    @Override
    public IColorControllerBuilder allowAlpha(boolean allowAlpha) {
        this.allowAlpha = allowAlpha;
        return this;
    }

    @Override
    public IController<Color> build() {
        return new ColorController(option, allowAlpha);
    }
}
