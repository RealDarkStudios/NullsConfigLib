package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IFloatSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.FloatSliderController;

public class FloatSliderControllerBuilder extends AbstractControllerBuilder<Float> implements IFloatSliderControllerBuilder {
    private float min, max, step;
    private IValueFormatter<Float> formatter = FloatSliderController.DEFAULT_FORMATTER::apply;
    
    public FloatSliderControllerBuilder(IOption<Float> option) {
        super(option);
    }

    @Override
    public IFloatSliderControllerBuilder range(Float min, Float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IFloatSliderControllerBuilder step(Float step) {
        this.step = step;
        return this;
    }

    @Override
    public IFloatSliderControllerBuilder formatValue(IValueFormatter<Float> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Float> build() {
        return FloatSliderController.createInternal(option, min, max, step, formatter);
    }
}
