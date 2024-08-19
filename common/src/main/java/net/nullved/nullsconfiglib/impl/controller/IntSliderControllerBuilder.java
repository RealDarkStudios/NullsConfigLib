package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IIntSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.IntegerSliderController;

public class IntSliderControllerBuilder extends AbstractControllerBuilder<Integer> implements IIntSliderControllerBuilder {
    private int min, max, step;
    private IValueFormatter<Integer> formatter = IntegerSliderController.DEFAULT_FORMATTER::apply;

    public IntSliderControllerBuilder(IOption<Integer> option) {
        super(option);
    }

    @Override
    public IIntSliderControllerBuilder range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IIntSliderControllerBuilder step(Integer step) {
        this.step = step;
        return this;
    }

    @Override
    public IIntSliderControllerBuilder formatValue(IValueFormatter<Integer> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Integer> build() {
        return IntegerSliderController.createInternal(option, min, max, step, formatter);
    }
}
