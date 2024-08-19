package net.nullved.nullsconfiglib;

import com.google.gson.GsonBuilder;
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
import net.nullved.nullsconfiglib.platform.NCLPlatform;

public class NCLConfig {
    private static final ConfigHelper<NCLConfig> HELPER = new ConfigHelper<>(NCLConfig.class);

    public static final StreamCodec<RegistryFriendlyByteBuf, NCLConfig> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HELPER.packetCodecGetter("showColorPickerIndicator"),
            ByteBufCodecs.BOOL, HELPER.packetCodecGetter("useDefaultButtons"),
            NCLConfig::new
    );

    /**
        Creates a config with all default values
     */
    public NCLConfig() {}

    /**
     * Creates a config with the specified values
     * @param showColorPickerIndicator Whether to show the color picker indicator or not
     * @param useDefaultButtons Whether to use the default buttons or not (Currently unable to edit in-game)
     */
    private NCLConfig(boolean showColorPickerIndicator, boolean useDefaultButtons) {
        this.showColorPickerIndicator = showColorPickerIndicator;
        this.useDefaultButtons = useDefaultButtons;
    }

    /**
     * The {@link IConfigHandler} for the NCL Common Config
     */
    public static final IConfigHandler<NCLConfig> HANDLER = IConfigHandler.createBuilder(NCLConfig.class)
            .id(NCLPlatform.rl("common"))
            .codec(PACKET_CODEC)
            .serializer(config -> IGsonConfigSerializerBuilder.create(config)
                    .setPath(Platform.getConfigFolder().resolve("ncl.json5"))
                    .setJson5(true)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .build())
            .build();

    /**
     * Whether to show the color picker indicator or not
     */
    @ConfigEntry
    @CategorySettings(category = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
    public boolean showColorPickerIndicator = true;

    /**
     * Whether to use Minecraft's buttons (should be compatible with all RPs) or the custom NCL buttons (currently only accessible by editing the config file)
     */
    @ConfigEntry
    @DisableOption
    @AssetReload
    @CategorySettings(category = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
    public boolean useDefaultButtons = true;
}