package net.nullved.nullsconfiglib.gui.controllers.slider;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

public class DoubleSliderController implements ISliderController<Double> {
    public static final Function<Double, Component> DEFAULT_FORMATTER = value -> Component.literal(String.format("%,d", value).replaceAll("[\u00a0\u202F]", " "));
    private final IOption<Double> option;
    private final double min, max, step;
    private final IValueFormatter<Double> valueFormatter;

    public DoubleSliderController(IOption<Double> option, double min, double max, double step) {
        this(option, min, max, step, DEFAULT_FORMATTER);
    }

    public DoubleSliderController(IOption<Double> option, double min, double max, double step, Function<Double, Component> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(step > 0, "`step` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.step = step;
        this.valueFormatter = valueFormatter::apply;
    }

    public static DoubleSliderController createInternal(IOption<Double> option, double min, double max, double step, IValueFormatter<Double> formatter) {
        return new DoubleSliderController(option, min, max, step, formatter::format);
    }
    
    @Override
    public IOption<Double> option() {
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
        option().requestSet(value);
    }
    
    @Override
    public double pendingValue() {
        return option().pendingValue();
    }

}
