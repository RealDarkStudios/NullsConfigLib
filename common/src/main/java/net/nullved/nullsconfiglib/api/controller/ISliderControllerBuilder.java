package net.nullved.nullsconfiglib.api.controller;

public interface ISliderControllerBuilder<T extends Number, B extends ISliderControllerBuilder<T, B>> extends IValueFormattableController<T, B> {
    B range(T min, T max);
    B step(T step);
}