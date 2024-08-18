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

package net.nullved.nullsconfiglib.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.utils.NCLRenderHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;

public abstract class AbstractWidget implements GuiEventListener, Renderable, NarratableEntry {
    protected final Minecraft client = Minecraft.getInstance();
    protected final Font textRenderer = client.font;
    protected final int inactiveColor = 0xFFA0A0A0;

    private Dimension<Integer> dim;

    public AbstractWidget(Dimension<Integer> dim) {
        this.dim = dim;
    }

    public boolean canReset() {
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (dim == null) return false;
        return this.dim.isPointInside((int) mouseX, (int) mouseY);
    }

    public void setDimension(Dimension<Integer> dim) {
        this.dim = dim;
    }

    public Dimension<Integer> getDimension() {
        return dim;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    public void unfocus() {
    }

    public boolean matchesSearch(String query) {
        return true;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
    }

    protected void drawButtonRect(GuiGraphics graphics, int x1, int y1, int x2, int y2, boolean hovered, boolean enabled) {
        if (x1 > x2) {
            int xx1 = x1;
            x1 = x2;
            x2 = xx1;
        }
        if (y1 > y2) {
            int yy1 = y1;
            y1 = y2;
            y2 = yy1;
        }
        int width = x2 - x1;
        int height = y2 - y1;

        NCLRenderHelper.renderButtonTexture(graphics, x1, y1, width, height, enabled, hovered);
    }

    protected void drawOutline(GuiGraphics graphics, int x1, int y1, int x2, int y2, int width, int color) {
        graphics.fill(x1, y1, x2, y1 + width, color);
        graphics.fill(x2, y1, x2 - width, y2, color);
        graphics.fill(x1, y2, x2, y2 - width, color);
        graphics.fill(x1, y1, x1 + width, y2, color);
    }

    protected void fillSidewaysGradient(GuiGraphics graphics, int x1, int y1, int x2, int y2, int startColor, int endColor) {
        //Fills a gradient, left to right
        //Uses practically the same method as the GuiGraphics class, but with the x/y moved
        //Has a custom "z" value in case needed for later
        VertexConsumer vertex = graphics.bufferSource().getBuffer(RenderType.gui());
        Matrix4f matrix4f = graphics.pose().last().pose();

        vertex.addVertex(matrix4f, x1, y1, 0).setColor(startColor);
        vertex.addVertex(matrix4f, x1, y2, 0).setColor(startColor);
        vertex.addVertex(matrix4f, x2, y2, 0).setColor(endColor);
        vertex.addVertex(matrix4f, x2, y1, 0).setColor(endColor);
    }


    protected void drawRainbowGradient(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
        //Draws a rainbow gradient, left to right
        int[] colors = new int[] {Color.red.getRGB(), Color.yellow.getRGB(), Color.green.getRGB(),
                Color.cyan.getRGB(), Color.blue.getRGB(), Color.magenta.getRGB(), Color.red.getRGB()}; //all the colors in the gradient
        int width = x2 - x1;
        int maxColors = colors.length - 1;
        for (int color = 0; color < maxColors; color++) {
            //First checks if the final color is being rendered, if true -> uses x2 int instead of x1
            //if false -> it adds the width divided by the max colors multiplied by the current color plus one to the x1 int
            //the x2 int for the fillSidewaysGradient is the same formula, excluding the additional plus one.
            //The gradient colors is determined by the color int and the color int plus one, which is why red is in the colors array twice
            fillSidewaysGradient(graphics,
                    x1 + (width / maxColors * color), y1,
                    color == maxColors - 1 ? x2 : x1 + (width / maxColors * (color + 1)), y2,
                    colors[color], colors[color + 1]);
        }
    }

    protected int multiplyColor(int hex, float amount) {
        Color color = new Color(hex, true);

        return new Color(Math.max((int)(color.getRed() * amount), 0),
                Math.max((int)(color.getGreen() * amount), 0),
                Math.max((int)(color.getBlue() * amount), 0),
                color.getAlpha()).getRGB();
    }

    public void playDownSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}