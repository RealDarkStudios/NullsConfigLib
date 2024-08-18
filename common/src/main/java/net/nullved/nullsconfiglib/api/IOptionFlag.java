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

import net.minecraft.client.Minecraft;
import net.nullved.nullsconfiglib.config.autogen.OptionFlagRegistry;
import net.nullved.nullsconfiglib.gui.RequireRestartScreen;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

@FunctionalInterface
public interface IOptionFlag extends Consumer<Minecraft> {
    IOptionFlag GAME_RESTART = client -> client.setScreen(new RequireRestartScreen(client.screen));
    IOptionFlag RELOAD_CHUNKS = client -> client.levelRenderer.allChanged();
    IOptionFlag WORLD_RENDER_UPDATE = client -> client.levelRenderer.needsUpdate();
    IOptionFlag ASSET_RELOAD = Minecraft::delayTextureReload;
}
