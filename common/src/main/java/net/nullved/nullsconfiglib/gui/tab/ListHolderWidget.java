package net.nullved.nullsconfiglib.gui.tab;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.nullved.nullsconfiglib.gui.ElementListWidgetExt;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ListHolderWidget<T extends ElementListWidgetExt<?>> extends AbstractWidget implements ContainerEventHandler {
    private final Supplier<ScreenRectangle> dimensions;
    private final T list;

    public ListHolderWidget(Supplier<ScreenRectangle> dimensions, T list) {
        super(0, 0, 100, 0, CommonComponents.EMPTY);
        this.dimensions = dimensions;
        this.list = list;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ScreenRectangle dimensions = this.dimensions.get();
        this.setX(dimensions.left());
        this.setY(dimensions.top());
        this.width = dimensions.width();
        this.height = dimensions.height();
        this.list.updateDimensions(dimensions);
        this.list.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.list.updateNarration(narrationElementOutput);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return ImmutableList.of(this.list);
    }

    public T getList() {
        return list;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.list.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.list.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.list.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return this.list.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.list.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.list.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isDragging() {
        return this.list.isDragging();
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.list.setDragging(isDragging);
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.list.getFocused();
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        this.list.setFocused(focused);
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        return this.list.nextFocusPath(event);
    }

    @Override
    public @Nullable ComponentPath getCurrentFocusPath() {
        return this.list.getCurrentFocusPath();
    }
}
