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

package net.nullved.nullsconfiglib.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.NullsConfigLib;
import org.apache.commons.lang3.NotImplementedException;

import java.nio.file.Path;

public class NCLPlatform {
    public static ResourceLocation parseRl(String rl) {
        return ResourceLocation.parse(rl);
    }

    public static ResourceLocation rl(String path) {
        return rl(NullsConfigLib.MOD_ID, path);
    }

    public static ResourceLocation mcRl(String path) {
        return rl("minecraft", path);
    }

    public static ResourceLocation rl(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    @ExpectPlatform
    public static Env getEnv() {
        throw new NotImplementedException("Not implemented!");
    }
}
