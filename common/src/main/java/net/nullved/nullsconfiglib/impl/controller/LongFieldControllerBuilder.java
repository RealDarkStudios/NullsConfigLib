package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.ILongFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.LongSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.LongFieldController;

public class LongFieldControllerBuilder extends AbstractControllerBuilder<Long> implements ILongFieldControllerBuilder {
    private long min = Long.MIN_VALUE;
    private long max = Long.MAX_VALUE;
    private IValueFormatter<Long> formatter = LongSliderController.DEFAULT_FORMATTER::apply;

    public LongFieldControllerBuilder(IOption<Long> option) {
        super(option);
    }

    @Override
    public LongFieldControllerBuilder min(Long min) {
        this.min = min;
        return this;
    }

    @Override
    public LongFieldControllerBuilder max(Long max) {
        this.max = max;
        return this;
    }

    @Override
    public LongFieldControllerBuilder range(Long min, Long max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public LongFieldControllerBuilder formatValue(IValueFormatter<Long> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Long> build() {
        return LongFieldController.createInternal(option, min, max, formatter);
    }
}