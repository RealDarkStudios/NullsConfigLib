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

package net.nullved.nullsconfiglib.gui;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;

public final class ValueFormatters {
    public static IValueFormatter<Float> percent(int decimalPlaces) {
        return new PercentFormatter(decimalPlaces);
    }

    public record PercentFormatter(int decimalPlaces) implements IValueFormatter<Float> {
        public PercentFormatter() {
            this(1);
        }

        @Override
        public Component format(Float value) {
            return Component.literal(String.format(".%d%f%%", decimalPlaces, value * 100));
        }
    }
}
