package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.impl.controller.CyclingListControllerBuilder;

public interface ICyclingListControllerBuilder<T> extends IValueFormattableController<T, ICyclingListControllerBuilder<T>> {
    @SuppressWarnings("unchecked")
    ICyclingListControllerBuilder<T> values(T... values);

    ICyclingListControllerBuilder<T> values(Iterable<? extends T> values);

    static <T> ICyclingListControllerBuilder<T> create(IOption<T> option) {
        return new CyclingListControllerBuilder<>(option);
    }
}
