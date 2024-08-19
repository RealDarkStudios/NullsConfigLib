package net.nullved.nullsconfiglib.gui.controllers.slider;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

public class IntegerSliderController implements ISliderController<Integer> {
    public static final Function<Integer, Component> DEFAULT_FORMATTER = value -> Component.literal(String.format("%,d", value).replaceAll("[\u00a0\u202F]", " "));
    private final IOption<Integer> option;
    private final int min, max, step;
    private final IValueFormatter<Integer> valueFormatter;

    public IntegerSliderController(IOption<Integer> option, int min, int max, int step) {
        this(option, min, max, step, DEFAULT_FORMATTER);
    }

    public IntegerSliderController(IOption<Integer> option, int min, int max, int step, Function<Integer, Component> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(step > 0, "`step` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.step = step;
        this.valueFormatter = valueFormatter::apply;
    }

    public static IntegerSliderController createInternal(IOption<Integer> option, int min, int max, int interval, IValueFormatter<Integer> formatter) {
        return new IntegerSliderController(option, min, max, interval, formatter::format);
    }

    @Override
    public IOption<Integer> option() {
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
        option().requestSet((int) value);
    }

    @Override
    public double pendingValue() {
        return option().pendingValue();
    }

}
