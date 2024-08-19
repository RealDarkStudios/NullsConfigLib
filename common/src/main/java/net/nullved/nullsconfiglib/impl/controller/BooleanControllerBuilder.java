package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IBooleanControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.BooleanController;
import org.apache.commons.lang3.Validate;

public class BooleanControllerBuilder extends AbstractControllerBuilder<Boolean> implements IBooleanControllerBuilder {
    private boolean colored = false;
    private IValueFormatter<Boolean> formatter = BooleanController.ON_OFF_FORMATTER::apply;

    public BooleanControllerBuilder(IOption<Boolean> option) {
        super(option);
    }

    @Override
    public IBooleanControllerBuilder colored(boolean colored) {
        this.colored = colored;
        return this;
    }

    @Override
    public IBooleanControllerBuilder formatValue(IValueFormatter<Boolean> formatter) {
        Validate.notNull(formatter, "`formatter` must not be null");

        this.formatter = formatter;
        return this;
    }

    @Override
    public IBooleanControllerBuilder onOffFormatter() {
        this.formatter = BooleanController.ON_OFF_FORMATTER::apply;
        return this;
    }

    @Override
    public IBooleanControllerBuilder yesNoFormatter() {
        this.formatter = BooleanController.YES_NO_FORMATTER::apply;
        return this;
    }

    @Override
    public IBooleanControllerBuilder trueFalseFormatter() {
        this.formatter = BooleanController.TRUE_FALSE_FORMATTER::apply;
        return this;
    }

    @Override
    public IController<Boolean> build() {
        return BooleanController.createInternal(option, formatter, colored);
    }
}
