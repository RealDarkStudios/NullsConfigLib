package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.EnumDropdownControllerBuilder;

public interface IEnumDropdownControllerBuilder<T extends Enum<T>> extends IValueFormattableController<T, IEnumDropdownControllerBuilder<T>> {
    static <T extends Enum<T>> IEnumDropdownControllerBuilder<T> create(IOption<T> option) {
        return new EnumDropdownControllerBuilder(option);
    }
}
