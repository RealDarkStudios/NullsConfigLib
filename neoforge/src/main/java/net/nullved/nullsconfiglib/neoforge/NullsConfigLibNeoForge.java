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

package net.nullved.nullsconfiglib.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.neoforged.fml.common.Mod;
import net.nullved.nullsconfiglib.platform.NCLConfig;

@Mod(NullsConfigLib.MOD_ID)
public final class NullsConfigLibNeoForge {
    public NullsConfigLibNeoForge() {
        NullsConfigLib.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> Screen.hasShiftDown() ?
                        NeoForgeTestConfig.HANDLER.generateGui().generateScreen(parent) :
                        NCLConfig.HANDLER.generateGui().generateScreen(parent)
        );
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForgeTestConfig.HANDLER.load();
    }

    public static void onClientStopping(GameShuttingDownEvent event) {
        NeoForgeTestConfig.HANDLER.save();
    }
}
