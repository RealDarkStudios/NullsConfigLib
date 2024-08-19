package net.nullved.nullsconfiglib.platform.fabric;

import dev.architectury.utils.Env;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class NCLPlatformImpl {
    public static Env getEnv() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> Env.CLIENT;
            case SERVER -> Env.SERVER;
        };
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
