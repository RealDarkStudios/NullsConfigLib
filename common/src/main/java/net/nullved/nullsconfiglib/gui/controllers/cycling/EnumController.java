package net.nullved.nullsconfiglib.gui.controllers.cycling;

import net.minecraft.network.chat.Component;
import net.minecraft.util.OptionEnum;
import net.nullved.nullsconfiglib.api.INameableEnum;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;

public class EnumController<T extends Enum<T>> extends CyclingListController<T> {
    public static <T extends Enum<T>> Function<T, Component> getDefaultFormatter() {
        return value -> {
            if (value instanceof INameableEnum nameableEnum) return nameableEnum.getDisplayName();
            if (value instanceof OptionEnum translatableOptions) return translatableOptions.getCaption();
            return Component.translatable(value.toString());
        };
    }

    public EnumController(IOption<T> option, Class<T> enumClass) {
        this(option, getDefaultFormatter(), enumClass.getEnumConstants());
    }

    public EnumController(IOption<T> option, Function<T, Component> valueFormatter, T[] availableValues) {
        super(option, Arrays.asList(availableValues), valueFormatter);
    }

    public static <T extends Enum<T>> EnumController<T> createInternal(IOption<T> option, IValueFormatter<T> formatter, T[] values) {
        return new EnumController<>(option, formatter::format, values);
    }
}
