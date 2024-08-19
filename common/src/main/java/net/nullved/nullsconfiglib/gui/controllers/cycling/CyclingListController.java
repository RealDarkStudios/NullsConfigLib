package net.nullved.nullsconfiglib.gui.controllers.cycling;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public class CyclingListController<T> implements ICyclingController<T> {
    private final IOption<T> option;
    private final IValueFormatter<T> valueFormatter;
    private final ImmutableList<T> values;

    public CyclingListController(IOption<T> option, Iterable<? extends T> values) {
        this(option, values, value -> Component.literal(value.toString()));
    }

    public CyclingListController(IOption<T> option, Iterable<? extends T> values, Function<T, Component> valueFormatter) {
        this.option = option;
        this.valueFormatter = valueFormatter::apply;
        this.values = ImmutableList.copyOf(values);
    }

    @ApiStatus.Internal
    public static <T> CyclingListController<T> createInternal(IOption<T> option, Iterable<? extends T> values, IValueFormatter<T> formatter) {
        return new CyclingListController<>(option, values, formatter::format);
    }

    @Override
    public IOption<T> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    @Override
    public void setPendingValue(int ordinal) {
        option().requestSet(values.get(ordinal));
    }

    @Override
    public int getPendingValue() {
        return values.indexOf(option().pendingValue());
    }

    @Override
    public int getCycleLength() {
        return values.size();
    }
}
