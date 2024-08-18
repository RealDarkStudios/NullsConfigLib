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

import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.nullved.nullsconfiglib.api.config.ConfigEntry;
import net.nullved.nullsconfiglib.api.config.ConfigHelper;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.api.config.autogen.*;
import net.nullved.nullsconfiglib.api.config.autogen.Boolean;
import net.nullved.nullsconfiglib.api.config.serializer.IGsonConfigSerializerBuilder;

public class NCLConfig {
    private static final ConfigHelper<NCLConfig> HELPER = new ConfigHelper<>(NCLConfig.class);
//    public static final Codec<NCLConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            HELPER.codecBuilder(Codec.BOOL, "showColorPickerIndicator"),
//            HELPER.codecBuilder(Codec.BOOL, "useDefaultButtons")
//    ).apply(instance, (a, b) -> NCLConfig.HANDLER.instance()));
    public static final StreamCodec<RegistryFriendlyByteBuf, NCLConfig> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HELPER.packetCodecGetter("showColorPickerIndicator"),
            ByteBufCodecs.BOOL, HELPER.packetCodecGetter("useDefaultButtons"),
            NCLConfig::new
    );

    /**
        Creates a config with all default values
     */
    public NCLConfig() {}

    private NCLConfig(boolean showColorPickerIndicator, boolean useDefaultButtons) {
        this.showColorPickerIndicator = showColorPickerIndicator;
        this.useDefaultButtons = useDefaultButtons;
    }

    public static final IConfigHandler<NCLConfig> HANDLER = IConfigHandler.createBuilder(NCLConfig.class)
            .id(NCLPlatform.rl("common"))
            .codec(PACKET_CODEC)
            .serializer(config -> IGsonConfigSerializerBuilder.create(config)
                    .setPath(Platform.getConfigFolder().resolve("ncl.json5"))
                    .setJson5(true)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .build())
            .build();
//    @ConfigEntry
//    @CategorySettings(category = "general")
//    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
//    public boolean requestServerConfig;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
    public boolean showColorPickerIndicator = true;

    @ConfigEntry
    @DisableOption
    @AssetReload
    @CategorySettings(category = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
    public boolean useDefaultButtons = true;
}