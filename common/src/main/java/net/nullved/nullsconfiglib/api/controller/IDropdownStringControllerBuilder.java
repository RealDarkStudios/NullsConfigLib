package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.DropdownStringControllerBuilder;

import java.util.List;

public interface IDropdownStringControllerBuilder extends IStringControllerBuilder {
    IDropdownStringControllerBuilder values(List<String> values);
    IDropdownStringControllerBuilder values(String... values);
    IDropdownStringControllerBuilder allowEmptyValue(boolean allowEmptyValue);
    IDropdownStringControllerBuilder allowAnyValue(boolean allowAnyValue);


    static IDropdownStringControllerBuilder create(IOption<String> option) {
        return new DropdownStringControllerBuilder(option);
    }
}
