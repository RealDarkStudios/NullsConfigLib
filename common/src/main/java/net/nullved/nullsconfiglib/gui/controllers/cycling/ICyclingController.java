package net.nullved.nullsconfiglib.gui.controllers.cycling;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public interface ICyclingController<T> extends IController<T> {
    void setPendingValue(int ordinal);

    int getPendingValue();

    int getCycleLength();

    @Override
    default AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new CyclingControllerElement(this, screen, widgetDimension);
    }
}
