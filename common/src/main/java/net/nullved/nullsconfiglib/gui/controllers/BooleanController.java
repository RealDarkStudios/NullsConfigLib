/*
 */

package net.nullved.nullsconfiglib.gui.controllers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

import static java.awt.SystemColor.control;

public class BooleanController implements IController<Boolean> {
    public static final Function<Boolean, Component> ON_OFF_FORMATTER = (state) ->
            state ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;

    public static final Function<Boolean, Component> TRUE_FALSE_FORMATTER = (state) ->
            state ? Component.translatable("ncl.control.boolean.true") : Component.translatable("ncl.control.boolean.false");

    public static final Function<Boolean, Component> YES_NO_FORMATTER = (state) ->
            state ? CommonComponents.GUI_YES : CommonComponents.GUI_NO;

    private final IOption<Boolean> option;
    private final IValueFormatter<Boolean> valueFormatter;
    private final boolean colored;

    public BooleanController(IOption<Boolean> option) {
        this(option, ON_OFF_FORMATTER, false);
    }

    public BooleanController(IOption<Boolean> option, boolean colored) {
        this(option, ON_OFF_FORMATTER, colored);
    }

    public BooleanController(IOption<Boolean> option, Function<Boolean, Component> valueFormatter, boolean colored) {
        this.option = option;
        this.valueFormatter = valueFormatter::apply;
        this.colored = colored;
    }

    @ApiStatus.Internal
    public static BooleanController createInternal(IOption<Boolean> option, IValueFormatter<Boolean> valueFormatter, boolean colored) {
        return new BooleanController(option, valueFormatter::format, colored);
    }

    @Override
    public IOption<Boolean> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    public boolean isColored() {
        return colored;
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new BooleanControllerElement(this, screen, widgetDimension);
    }

    public static class BooleanControllerElement extends ControllerWidget<BooleanController> {
        public BooleanControllerElement(BooleanController controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
            super(controller, screen, widgetDimension);
        }

        @Override
        protected void drawHoveredControl(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY) || !isAvailable())
                return false;

            toggleSetting();
            return true;
        }

        @Override
        protected int getHoveredControlWidth() {
            return getUnhoveredControlWidth();
        }

        public void toggleSetting() {
            controller.option().requestSet(!controller.option().pendingValue());
            playDownSound();
        }

        @Override
        protected Component getValueText() {
            if (controller.isColored()) {
                return super.getValueText().copy().withStyle(controller.option().pendingValue() ? ChatFormatting.GREEN : ChatFormatting.RED);
            }

            return super.getValueText();
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!isFocused()) {
                return false;
            }

            if (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_SPACE || keyCode == InputConstants.KEY_NUMPADENTER) {
                toggleSetting();
                return true;
            }

            return false;
        }
    }
}
