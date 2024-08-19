package net.nullved.nullsconfiglib.platform;

import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.NullsConfigLib;

public class NCLPlatform {
    /**
     * Parse a {@link ResourceLocation} from a string
     * @param rl The string to parse
     * @return The parsed {@link ResourceLocation}
     */
    public static ResourceLocation parseRl(String rl) {
        return ResourceLocation.parse(rl);
    }

    /**
     * Creates a {@link ResourceLocation} with the {@link NullsConfigLib#MOD_ID} namespace
     * @param path The path of the {@link ResourceLocation}
     * @return The created {@link ResourceLocation}
     */
    public static ResourceLocation rl(String path) {
        return rl(NullsConfigLib.MOD_ID, path);
    }

    /**
     * Creates a {@link ResourceLocation} with the "minecraft" namespace
     * @param path The path of the {@link ResourceLocation}
     * @return The created {@link ResourceLocation}
     */
    public static ResourceLocation mcRl(String path) {
        return rl("minecraft", path);
    }


    /**
     * Creates a {@link ResourceLocation}
     * @param namespace The namespace of the {@link ResourceLocation}
     * @param path The path of the {@link ResourceLocation}
     * @return The created {@link ResourceLocation}
     */
    public static ResourceLocation rl(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
