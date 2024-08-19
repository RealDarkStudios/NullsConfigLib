package net.nullved.nullsconfiglib.api.controller;

public interface INumberFieldControllerBuilder<T extends Number, B extends INumberFieldControllerBuilder<T, B>> extends IValueFormattableController<T, B> {
    B min(T min);
    B max(T max);
    B range(T min, T max);
}
