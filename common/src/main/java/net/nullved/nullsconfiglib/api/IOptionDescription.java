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

package net.nullved.nullsconfiglib.api;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.impl.OptionDescription;

import java.util.Collection;

public interface IOptionDescription {
    IOptionDescription EMPTY = new OptionDescription(CommonComponents.EMPTY);
    Component text();

    static Builder createBuilder() {
        return new OptionDescription.Builder();
    }

    static IOptionDescription of(Component... description) {
        return createBuilder().text(description).build();
    }

    interface Builder {
        Builder text(Component... description);
        Builder text(Collection<? extends Component> text);

        IOptionDescription build();
    }
}
