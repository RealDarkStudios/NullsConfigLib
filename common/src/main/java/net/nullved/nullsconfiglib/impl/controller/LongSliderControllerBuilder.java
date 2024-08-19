package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.ILongSliderControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.LongSliderController;

public class LongSliderControllerBuilder extends AbstractControllerBuilder<Long> implements ILongSliderControllerBuilder {
    private long min, max, step;
    private IValueFormatter<Long> formatter = LongSliderController.DEFAULT_FORMATTER::apply;

    public LongSliderControllerBuilder(IOption<Long> option) {
        super(option);
    }

    @Override
    public ILongSliderControllerBuilder range(Long min, Long max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public ILongSliderControllerBuilder step(Long step) {
        this.step = step;
        return this;
    }

    @Override
    public ILongSliderControllerBuilder formatValue(IValueFormatter<Long> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Long> build() {
        return LongSliderController.createInternal(option, min, max, step, formatter);
    }
}
