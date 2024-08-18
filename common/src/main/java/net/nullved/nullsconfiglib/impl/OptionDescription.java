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

package net.nullved.nullsconfiglib.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nullved.nullsconfiglib.api.IOptionDescription;

import java.util.*;

public record OptionDescription(Component text) implements IOptionDescription {
    public static class Builder implements IOptionDescription.Builder {
        private final List<Component> descriptionLines = new ArrayList<>();

        @Override
        public IOptionDescription.Builder text(Component... description) {
            this.descriptionLines.addAll(Arrays.asList(description));
            return this;
        }

        @Override
        public IOptionDescription.Builder text(Collection<? extends Component> text) {
            this.descriptionLines.addAll(text);
            return this;
        }

        @Override
        public IOptionDescription build() {
            MutableComponent concatenatedDescription = Component.empty();
            Iterator<Component> iter = descriptionLines.iterator();
            while (iter.hasNext()) {
                concatenatedDescription.append(iter.next());
                if (iter.hasNext()) concatenatedDescription.append("\n");
            }

            return new OptionDescription(concatenatedDescription);
        }
    }
}
