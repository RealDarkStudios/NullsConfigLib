package net.nullved.nullsconfiglib.gui.controllers.string.number;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.slider.LongSliderController;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class LongFieldController extends NumberFieldController<Long> {
    private final long min, max;

    public LongFieldController(IOption<Long> option, long min, long max, Function<Long, Component> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public LongFieldController(IOption<Long> option, long min, long max) {
        this(option, min, max, LongSliderController.DEFAULT_FORMATTER);
    }

    public LongFieldController(IOption<Long> option, Function<Long, Component> formatter) {
        this(option, -Long.MAX_VALUE, Long.MAX_VALUE, formatter);
    }

    public LongFieldController(IOption<Long> option) {
        this(option, -Long.MAX_VALUE, Long.MAX_VALUE, LongSliderController.DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static LongFieldController createInternal(IOption<Long> option, long min, long max, IValueFormatter<Long> formatter) {
        return new LongFieldController(option, min, max, formatter::format);
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
        option().requestSet((long) value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
