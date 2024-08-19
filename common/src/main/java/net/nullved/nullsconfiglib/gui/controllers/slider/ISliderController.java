package net.nullved.nullsconfiglib.gui.controllers.slider;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

public interface ISliderController<T extends Number> extends IController<T> {
    double min();
    double max();
    double step();

    default double range() {
        return max() - min();
    }

    void setPendingValue(double value);

    double pendingValue();

    @Override
    default AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new SliderControllerElement(this, screen, widgetDimension, min(), max(), step());
    }
}
