package net.nullved.nullsconfiglib.gui.controllers.string.number;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.FloatSliderController;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class FloatFieldController extends NumberFieldController<Float> {
    private final float min, max;

    public FloatFieldController(IOption<Float> option, float min, float max, Function<Float, Component> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public FloatFieldController(IOption<Float> option, float min, float max) {
        this(option, min, max, FloatSliderController.DEFAULT_FORMATTER);
    }

    public FloatFieldController(IOption<Float> option, Function<Float, Component> formatter) {
        this(option, -Float.MAX_VALUE, Float.MAX_VALUE, formatter);
    }

    public FloatFieldController(IOption<Float> option) {
        this(option, -Float.MAX_VALUE, Float.MAX_VALUE, FloatSliderController.DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static FloatFieldController createInternal(IOption<Float> option, float min, float max, IValueFormatter<Float> formatter) {
        return new FloatFieldController(option, min, max, formatter::format);
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
        option().requestSet((float) value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
