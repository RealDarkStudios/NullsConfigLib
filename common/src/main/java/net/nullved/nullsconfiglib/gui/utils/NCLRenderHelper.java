package net.nullved.nullsconfiglib.gui.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.platform.NCLPlatform;

public class NCLRenderHelper {
    private static final WidgetSprites MINECRAFT_BUTTON_SPRITES = new WidgetSprites(
            NCLPlatform.mcRl("widget/button"),
            NCLPlatform.mcRl("widget/button_disabled"),
            NCLPlatform.mcRl("widget/button_highlighted"),
            NCLPlatform.mcRl("widget/slider_highlighted")
    );

    private static final WidgetSprites TRANSPARENT_BUTTON_SPRITES = new WidgetSprites(
            NCLPlatform.rl("widget/button"),
            NCLPlatform.rl("widget/button_disabled"),
            NCLPlatform.rl("widget/button_highlighted"),
            NCLPlatform.rl("widget/slider_highlighted")
    );

    public static void renderButtonTexture(GuiGraphics guiGraphics, int x, int y, int width, int height, boolean enabled, boolean focused) {
        if (NullsConfigLib.config.useDefaultButtons) {
            guiGraphics.blitSprite(MINECRAFT_BUTTON_SPRITES.get(enabled, focused), x, y, width, height);
        } else {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            guiGraphics.blitSprite(TRANSPARENT_BUTTON_SPRITES.get(enabled, focused), x, y, width, height);

            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    public static ResourceLocation getSpriteLocation(String path) {
        return NCLPlatform.rl(path);
    }
}
