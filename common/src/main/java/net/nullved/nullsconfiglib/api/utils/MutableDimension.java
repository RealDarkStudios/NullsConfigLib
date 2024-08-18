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

public interface MutableDimension<T extends Number> extends Dimension<T> {
    MutableDimension<T> setX(T x);
    MutableDimension<T> setY(T y);
    MutableDimension<T> setWidth(T width);
    MutableDimension<T> setHeight(T height);

    MutableDimension<T> move(T x, T y);
    MutableDimension<T> expand(T width, T height);
}
