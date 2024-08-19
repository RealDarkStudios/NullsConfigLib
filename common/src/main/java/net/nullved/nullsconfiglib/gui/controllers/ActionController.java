/*
 */

package net.nullved.nullsconfiglib.gui.controllers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IButtonOption;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.function.BiConsumer;

public class ActionController implements IController<BiConsumer<NCLScreen, IButtonOption>> {
    public static final Component DEFAULT_TEXT = Component.translatable("ncl.control.action.execute");

    private final IButtonOption option;
    private final Component text;

    public ActionController(IButtonOption option) {
        this(option, DEFAULT_TEXT);
    }

    public ActionController(IButtonOption option, Component text) {
        this.option = option;
        this.text = text;
    }

    @Override
    public IButtonOption option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return text;
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new ActionControllerElement(this, screen, widgetDimension);
    }

    public static class ActionControllerElement extends ControllerWidget<ActionController> {
        private final String buttonString;

        public ActionControllerElement(ActionController controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
            super(controller, screen, widgetDimension);
            buttonString = controller.formatValue().getString().toLowerCase();
        }

        public void executeAction() {
            playDownSound();
            controller.option().action().accept(screen, controller.option());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY) && isAvailable()) {
                executeAction();
                return true;
            }
            return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!focused) return false;

            if (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_SPACE || keyCode == InputConstants.KEY_NUMPADENTER) {
                executeAction();
                return true;
            }

            return false;
        }

        @Override
        protected int getHoveredControlWidth() {
            return getUnhoveredControlWidth();
        }

        @Override
        public boolean canReset() {
            return false;
        }

        @Override
        public boolean matchesSearch(String query) {
            return super.matchesSearch(query) || buttonString.contains(query);
        }
    }
}
