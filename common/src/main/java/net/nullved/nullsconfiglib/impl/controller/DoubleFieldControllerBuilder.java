package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IDoubleFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.DoubleSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.DoubleFieldController;

public class DoubleFieldControllerBuilder extends AbstractControllerBuilder<Double> implements IDoubleFieldControllerBuilder {
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private IValueFormatter<Double> formatter = DoubleSliderController.DEFAULT_FORMATTER::apply;

    public DoubleFieldControllerBuilder(IOption<Double> option) {
        super(option);
    }

    @Override
    public DoubleFieldControllerBuilder min(Double min) {
        this.min = min;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder max(Double max) {
        this.max = max;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder range(Double min, Double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public DoubleFieldControllerBuilder formatValue(IValueFormatter<Double> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Double> build() {
        return DoubleFieldController.createInternal(option, min, max, formatter);
    }
}