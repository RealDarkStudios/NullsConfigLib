package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IDoubleSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.DoubleSliderController;

public class DoubleSliderControllerBuilder extends AbstractControllerBuilder<Double> implements IDoubleSliderControllerBuilder {
    private double min, max, step;
    private IValueFormatter<Double> formatter = DoubleSliderController.DEFAULT_FORMATTER::apply;

    public DoubleSliderControllerBuilder(IOption<Double> option) {
        super(option);
    }

    @Override
    public IDoubleSliderControllerBuilder range(Double min, Double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IDoubleSliderControllerBuilder step(Double step) {
        this.step = step;
        return this;
    }

    @Override
    public IDoubleSliderControllerBuilder formatValue(IValueFormatter<Double> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Double> build() {
        return DoubleSliderController.createInternal(option, min, max, step, formatter);
    }
}
