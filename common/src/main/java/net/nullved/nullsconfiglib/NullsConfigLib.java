package net.nullved.nullsconfiglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NullsConfigLib {
    /**
     * NCL's Mod ID
     */
    public static final String MOD_ID = "nullsconfiglib";
    /**
     * NCL's Logger
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    /**
     * The config instance of {@link NCLConfig}, also accessible through {@link #getConfig()} or {@code NCLConfig.HANDLER#getInstance()}
     */
    public static NCLConfig config;

    /**
     * Static init method, called from loader-specific entrypoints
     */
    public static void init() {
        NCLConfig.HANDLER.load();

        config = NCLConfig.HANDLER.instance();
    }

    /**
     * Gets the config instance
     * @return The config instance
     */
    public static NCLConfig getConfig() {
        return config;
    }
}
