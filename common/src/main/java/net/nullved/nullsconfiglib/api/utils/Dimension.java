/*
 * NullsConfigLib - A Config Library for Null's Mods
 * Copyright (C) 2024 NullVed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
