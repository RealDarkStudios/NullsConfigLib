package net.nullved.nullsconfiglib.impl.controller;

import com.google.common.collect.ImmutableList;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.ICyclingListControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.cycling.CyclingListController;

public class CyclingListControllerBuilder<T> extends AbstractControllerBuilder<T> implements ICyclingListControllerBuilder<T> {
    private Iterable<? extends T> values;
    private IValueFormatter<T> formatter = null;

    public CyclingListControllerBuilder(IOption<T> option) {
        super(option);
    }

    @Override
    public ICyclingListControllerBuilder<T> values(Iterable<? extends T> values) {
        this.values = values;
        return this;
    }

    @Override
    public ICyclingListControllerBuilder<T> values(T... values) {
        this.values = ImmutableList.copyOf(values);
        return this;
    }

    @Override
    public ICyclingListControllerBuilder<T> formatValue(IValueFormatter<T> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<T> build() {
        return CyclingListController.createInternal(option, values, formatter);
    }
}
