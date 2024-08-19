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
