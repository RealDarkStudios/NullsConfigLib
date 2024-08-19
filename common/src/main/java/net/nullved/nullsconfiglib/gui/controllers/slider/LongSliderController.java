package net.nullved.nullsconfiglib.gui.controllers.slider;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

public class LongSliderController implements ISliderController<Long> {
    public static final Function<Long, Component> DEFAULT_FORMATTER = value -> Component.literal(String.format("%,d", value).replaceAll("[\u00a0\u202F]", " "));
    private final IOption<Long> option;
    private final long min, max, step;
    private final IValueFormatter<Long> valueFormatter;

    public LongSliderController(IOption<Long> option, long min, long max, long step) {
        this(option, min, max, step, DEFAULT_FORMATTER);
    }

    public LongSliderController(IOption<Long> option, long min, long max, long step, Function<Long, Component> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(step > 0, "`step` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.step = step;
        this.valueFormatter = valueFormatter::apply;
    }

    public static LongSliderController createInternal(IOption<Long> option, long min, long max, long interval, IValueFormatter<Long> formatter) {
        return new LongSliderController(option, min, max, interval, formatter::format);
    }

    @Override
    public IOption<Long> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return valueFormatter.format(option().pendingValue());
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
    public double step() {
        return step;
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
