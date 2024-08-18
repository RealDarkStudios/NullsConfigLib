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

package net.nullved.nullsconfiglib.gui.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GuiUtils {
    public static MutableComponent translatableFallback(String key, Component fallback) {
        if (Language.getInstance().has(key))
            return Component.translatable(key);
        return fallback.copy();
    }

    public static String shortenString(String string, Font font, int maxWidth, String suffix) {
        if (string.isEmpty())
            return string;

        boolean firstIter = true;
        while (font.width(string) > maxWidth) {
            string = string.substring(0, Math.max(string.length() - 1 - (firstIter ? 1 : suffix.length() + 1), 0)).trim();
            string += suffix;

            if (string.equals(suffix))
                break;

            firstIter = false;
        }

        return string;
    }
}
