package net.nullved.nullsconfiglib.gui.controllers.string;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public interface IStringController<T> extends IController<T> {
    String getString();

    void setFromString(String value);

    @Override
    default Component formatValue() {
        return Component.literal(getString());
    }

    default boolean isInputValid(String input) {
        return true;
    }

    @Override
    default AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new StringControllerElement(this, screen, widgetDimension, true);
    }
}
