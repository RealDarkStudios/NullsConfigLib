package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IIntFieldControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.IntegerSliderController;
import net.nullved.nullsconfiglib.gui.controllers.string.number.IntegerFieldController;

public class IntFieldControllerBuilder extends AbstractControllerBuilder<Integer> implements IIntFieldControllerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;
    private IValueFormatter<Integer> formatter = IntegerSliderController.DEFAULT_FORMATTER::apply;

    public IntFieldControllerBuilder(IOption<Integer> option) {
        super(option);
    }

    @Override
    public IntFieldControllerBuilder min(Integer min) {
        this.min = min;
        return this;
    }

    @Override
    public IntFieldControllerBuilder max(Integer max) {
        this.max = max;
        return this;
    }

    @Override
    public IntFieldControllerBuilder range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IntFieldControllerBuilder formatValue(IValueFormatter<Integer> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<Integer> build() {
        return IntegerFieldController.createInternal(option, min, max, formatter);
    }
}