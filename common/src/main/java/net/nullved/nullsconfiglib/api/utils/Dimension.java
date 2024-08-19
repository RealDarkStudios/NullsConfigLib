package net.nullved.nullsconfiglib.api.utils;

import net.nullved.nullsconfiglib.impl.utils.DimensionInteger;

public interface Dimension<T extends Number> {
    T x();
    T y();

    T width();
    T height();

    T xLimit();
    T yLimit();

    T centerX();
    T centerY();

    boolean isPointInside(T x, T y);

    MutableDimension<T> clone();

    Dimension<T> withX(T x);
    Dimension<T> withY(T y);
    Dimension<T> withWidth(T width);
    Dimension<T> withHeight(T height);

    Dimension<T> moved(T x, T y);
    Dimension<T> expanded(T width, T height);

    static MutableDimension<Integer> ofInt(int x, int y, int width, int height) {
        return new DimensionInteger(x, y, width, height);
    }
}
