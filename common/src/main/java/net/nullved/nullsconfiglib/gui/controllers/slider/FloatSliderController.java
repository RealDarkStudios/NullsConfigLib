package net.nullved.nullsconfiglib.gui.controllers.slider;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

public class FloatSliderController implements ISliderController<Float> {
    public static final Function<Float, Component> DEFAULT_FORMATTER = value -> Component.literal(String.format("%,f", value).replaceAll("[\u00a0\u202F]", " "));
    private final IOption<Float> option;
    private final float min, max, step;
    private final IValueFormatter<Float> valueFormatter;

    public FloatSliderController(IOption<Float> option, float min, float max, float step) {
        this(option, min, max, step, DEFAULT_FORMATTER);
    }

    public FloatSliderController(IOption<Float> option, float min, float max, float step, Function<Float, Component> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(step > 0, "`step` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.step = step;
        this.valueFormatter = valueFormatter::apply;
    }

    public static FloatSliderController createInternal(IOption<Float> option, float min, float max, float step, IValueFormatter<Float> formatter) {
        return new FloatSliderController(option, min, max, step, formatter::format);
    }
    
    @Override
    public IOption<Float> option() {
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
        option().requestSet((float) value);
    }
    
    @Override
    public double pendingValue() {
        return option().pendingValue();
    }

}
