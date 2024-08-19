package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IDropdownStringControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.dropdown.DropdownStringController;

import java.util.Arrays;
import java.util.List;

public class DropdownStringControllerBuilder extends StringControllerBuilder implements IDropdownStringControllerBuilder {
    private List<String> values;
    private boolean allowEmptyValue = false;
    public boolean allowAnyValue = false;

    public DropdownStringControllerBuilder(IOption<String> option) {
        super(option);
    }

    @Override
    public IDropdownStringControllerBuilder values(List<String> values) {
        this.values = values;
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder values(String... values) {
        this.values = Arrays.asList(values);
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder allowEmptyValue(boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        return this;
    }

    @Override
    public IDropdownStringControllerBuilder allowAnyValue(boolean allowAnyValue) {
        this.allowAnyValue = allowAnyValue;
        return this;
    }

    @Override
    public IController<String> build() {
        return new DropdownStringController(option, values, allowEmptyValue, allowAnyValue);
    }
}
