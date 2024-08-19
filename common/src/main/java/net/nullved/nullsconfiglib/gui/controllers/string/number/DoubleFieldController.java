package net.nullved.nullsconfiglib.gui.controllers.string.number;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.DoubleSliderController;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class DoubleFieldController extends NumberFieldController<Double> {
    private final double min, max;

    public DoubleFieldController(IOption<Double> option, double min, double max, Function<Double, Component> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public DoubleFieldController(IOption<Double> option, double min, double max) {
        this(option, min, max, DoubleSliderController.DEFAULT_FORMATTER);
    }

    public DoubleFieldController(IOption<Double> option, Function<Double, Component> formatter) {
        this(option, -Double.MAX_VALUE, Double.MAX_VALUE, formatter);
    }

    public DoubleFieldController(IOption<Double> option) {
        this(option, -Double.MAX_VALUE, Double.MAX_VALUE, DoubleSliderController.DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static DoubleFieldController createInternal(IOption<Double> option, double min, double max, IValueFormatter<Double> formatter) {
        return new DoubleFieldController(option, min, max, formatter::format);
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
    }

    @Override
    public String getString() {
        return NUMBER_FORMAT.format(option().pendingValue());
    }

    @Override
    public void setPendingValue(double value) {
        option().requestSet(value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
