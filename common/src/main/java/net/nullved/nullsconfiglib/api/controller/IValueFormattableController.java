package net.nullved.nullsconfiglib.api.controller;

public interface IValueFormattableController<T, B extends IValueFormattableController<T, B>> extends IControllerBuilder<T> {
    B formatValue(IValueFormatter<T> formatter);
}
