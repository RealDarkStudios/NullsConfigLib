package net.nullved.nullsconfiglib.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class ElementListWidgetExt<E extends ElementListWidgetExt.Entry<E>> extends ContainerObjectSelectionList<E> implements LayoutElement {
    protected static final int SCROLLBAR_WIDTH = 6;

    private double smoothScrollAmount = getScrollAmount();
    private boolean returnSmoothAmount = false;
    private final boolean doSmoothScrolling;
    private boolean usingScrollBar;

    public ElementListWidgetExt(Minecraft client, int x, int y, int width, int height, boolean doSmoothScrolling) {
        super(client, width, x, y, height);
        this.doSmoothScrolling = doSmoothScrolling;
        setRenderHeader(false, 0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        double scroll = scrollY;
        scroll += scrollX;

        this.setScrollAmount(this.getScrollAmount() - scroll * 20);
        return true;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getX() + this.getWidth() - SCROLLBAR_WIDTH;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (usingScrollBar) {
            resetSmoothScrolling();
        }

        smoothScrollAmount = Mth.lerp(
                partialTick * 0.5,
                smoothScrollAmount,
                getScrollAmount()
        );
        returnSmoothAmount = true;

        guiGraphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.disableScissor();

        returnSmoothAmount = false;
    }

    @Override
    protected boolean isValidMouseClick(int button) {
        return button == InputConstants.MOUSE_BUTTON_LEFT || button == InputConstants.MOUSE_BUTTON_RIGHT;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && mouseX >= getScrollbarPosition() && mouseX < getScrollbarPosition() + SCROLLBAR_WIDTH) {
            usingScrollBar = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            usingScrollBar = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void updateDimensions(ScreenRectangle rectangle) {
        this.setX(rectangle.left());
        this.setY(rectangle.top());
        this.setWidth(rectangle.width());
        this.setHeight(rectangle.height());
    }

    @Override
    public double getScrollAmount() {
        if (returnSmoothAmount && doSmoothScrolling) return smoothScrollAmount;

        return super.getScrollAmount();
    }

    protected void resetSmoothScrolling() {
        this.smoothScrollAmount = super.getScrollAmount();
    }

    @Override
    protected @Nullable E getEntryAtPosition(double mouseX, double mouseY) {
        mouseY += getScrollAmount();

        if (mouseX < this.getX() || mouseX > this.getX() + this.getWidth())
            return null;

        int currentY = this.getY() - headerHeight + 4;
        for (E entry : children()) {
            if (mouseY >= currentY && mouseY <= currentY + entry.getItemHeight()) {
                return entry;
            }

            currentY += entry.getItemHeight();
        }

        return null;
    }

    /*
      below code is licensed from cloth-config under LGPL3
      modified to inherit vanilla's EntryListWidget and use yarn mappings

      code is responsible for having dynamic item heights
    */

    @Override
    protected int getMaxPosition() {
        return children().stream().map(E::getItemHeight).reduce(0, Integer::sum) + headerHeight;
    }

    @Override
    protected void centerScrollOn(E entry) {
        double d = (this.height) / -2d;
        for (int i = 0; i < this.children().indexOf(entry) && i < this.getItemCount(); i++)
            d += children().get(i).getItemHeight();
        this.setScrollAmount(d);
    }

    @Override
    protected int getRowTop(int index) {
        int integer = getY() + 4 - (int) this.getScrollAmount() + headerHeight;
        for (int i = 0; i < children().size() && i < index; i++)
            integer += children().get(i).getItemHeight();
        return integer;
    }

    @Override
    protected void ensureVisible(E entry) {
        int i = this.getRowTop(this.children().indexOf(entry));
        int j = i - this.getY() - 4 - entry.getItemHeight();
        if (j < 0) {
            this.setScrollAmount(this.getScrollAmount() + j);
        }

        int k = this.getY() + this.getHeight()  - i - entry.getItemHeight() * 2;
        if (k < 0) {
            this.setScrollAmount(this.getScrollAmount() - k);
        }
    }

    @Override
    protected void renderListItems(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int left = this.getRowLeft();
        int right = this.getRowWidth();
        int count = this.getItemCount();

        for(int i = 0; i < count; ++i) {
            E entry = children().get(i);
            int top = this.getRowTop(i);
            int bottom = top + entry.getItemHeight();
            int entryHeight = entry.getItemHeight() - 4;
            if (bottom >= this.getY() && top <= this.getY() + this.getHeight()) {
                this.renderItem(graphics, mouseX, mouseY, delta, i, left, top, right, entryHeight);
            }
        }
    }

    /* end cloth config code */

    public abstract static class Entry<E extends Entry<E>> extends ContainerObjectSelectionList.Entry<E> {
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            for (GuiEventListener child : this.children()) {
                if (child.mouseClicked(mouseX, mouseY, button)) {
                    if (button == InputConstants.MOUSE_BUTTON_LEFT || button == InputConstants.MOUSE_BUTTON_RIGHT)
                        this.setDragging(true);
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            if (isDragging() && (button == InputConstants.MOUSE_BUTTON_LEFT || button == InputConstants.MOUSE_BUTTON_RIGHT)) {
                for (GuiEventListener child : this.children()) {
                    if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
                        return true;
                }
            }
            return false;
        }

        public int getItemHeight() {
            return 22;
        }
    }
}
