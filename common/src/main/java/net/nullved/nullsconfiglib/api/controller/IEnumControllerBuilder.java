package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.EnumControllerBuilder;

public interface IEnumControllerBuilder<T extends Enum<T>> extends IValueFormattableController<T, IEnumControllerBuilder<T>> {
    IEnumControllerBuilder<T> enumClass(Class<T> enumClass);

    static <T extends Enum<T>> IEnumControllerBuilder<T> create(IOption<T> option) {
        return new EnumControllerBuilder(option);
    }
}
