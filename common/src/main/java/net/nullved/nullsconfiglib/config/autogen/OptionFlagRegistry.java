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

package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.*;
import net.nullved.nullsconfiglib.platform.NCLPlatform;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class OptionFlagRegistry {
    private static final Map<Class<?>, IOptionFlagInfo> flagInfoMap = new HashMap<>();
    private static final Map<IOptionFlag, Class<?>> flagToAnnotationMap = new HashMap<>();

    static {
        registerFlag(RequireRestart.class, "game_restart", IOptionFlag.GAME_RESTART);
        registerFlag(ReloadChunks.class, "reload_chunks", IOptionFlag.RELOAD_CHUNKS);
        registerFlag(WorldRenderUpdate.class, "world_render_update", IOptionFlag.WORLD_RENDER_UPDATE);
        registerFlag(AssetReload.class, "asset_reload", IOptionFlag.ASSET_RELOAD);
    }

    private static ResourceLocation defaultTexture(String path) {
        return NCLPlatform.rl("ncl/flag/" + path);
    }

    public static <A extends Annotation> void registerFlag(Class<A> annotation, String name, IOptionFlag flag) {
        registerFlag(annotation, name, flag, null);
    }

    public static <A extends Annotation> void registerFlag(Class<A> annotation, String name, IOptionFlag flag, ResourceLocation icon) {
        flagInfoMap.put(annotation, new IOptionFlagInfo(flag, name, icon == null ? defaultTexture(name) : icon));
        flagToAnnotationMap.put(flag, annotation);
    }

    public static List<IOptionFlag> getFlags(Field field, IConfigField<?> configField, IOptionAccess storage) {
        Annotation[] annotations = Arrays.stream(field.getAnnotations())
                .filter(annotation -> flagInfoMap.containsKey(annotation.annotationType()))
                .toArray(Annotation[]::new);

        return Arrays.stream(annotations)
                .map(ann -> flagInfoMap.get(ann.annotationType()).flag())
                .filter(Objects::nonNull)
                .toList();
    }

    public static boolean hasIcon(IOptionFlag flag) {
        return Minecraft.getInstance().getResourceManager().getResource(flagInfoMap.get(flagToAnnotationMap.get(flag)).icon().withPrefix("textures/gui/sprites/").withSuffix(".png")).isPresent();
    }

    public static ResourceLocation getIcon(IOptionFlag flag) {
        return flagInfoMap.get(flagToAnnotationMap.get(flag)).icon();
    }

    public static Component getTooltip(IOptionFlag flag) {
        return Component.translatable("ncl.flag.%s".formatted(flagInfoMap.get(flagToAnnotationMap.get(flag)).name()));
    }

    public record IOptionFlagInfo(IOptionFlag flag, String name, ResourceLocation icon) {
    }
}
