package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IFloatFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.FloatSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.FloatFieldController;

public class FloatFieldControllerBuilder extends AbstractControllerBuilder<Float> implements IFloatFieldControllerBuilder {
    private float min = Float.MIN_VALUE;
    private float max = Float.MAX_VALUE;
    private IValueFormatter<Float> formatter = FloatSliderController.DEFAULT_FORMATTER::apply;

    public FloatFieldControllerBuilder(IOption<Float> option) {
        super(option);
    }

    @Override
    public FloatFieldControllerBuilder min(Float min) {
        this.min = min;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder max(Float max) {
        this.max = max;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder range(Float min, Float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public FloatFieldControllerBuilder formatValue(IValueFormatter<Float> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Float> build() {
        return FloatFieldController.createInternal(option, min, max, formatter);
    }
}