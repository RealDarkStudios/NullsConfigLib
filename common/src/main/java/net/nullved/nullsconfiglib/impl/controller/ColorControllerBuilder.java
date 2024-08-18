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

package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IColorControllerBuilder;
import net.nullved.nullsconfiglib.gui.controllers.ColorController;

import java.awt.Color;

public class ColorControllerBuilder extends AbstractControllerBuilder<Color> implements IColorControllerBuilder {
    private boolean allowAlpha = false;

    public ColorControllerBuilder(IOption<Color> option) {
        super(option);
    }

    @Override
    public IColorControllerBuilder allowAlpha(boolean allowAlpha) {
        this.allowAlpha = allowAlpha;
        return this;
    }

    @Override
    public IController<Color> build() {
        return new ColorController(option, allowAlpha);
    }
}
