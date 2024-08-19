package net.nullved.nullsconfiglib.api;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public interface IController<T> {
    IOption<T> option();

    Component formatValue();

    AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension);
}
