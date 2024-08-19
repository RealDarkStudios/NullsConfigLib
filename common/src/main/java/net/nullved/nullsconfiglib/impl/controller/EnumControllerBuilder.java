package net.nullved.nullsconfiglib.impl.controller;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IEnumControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.cycling.EnumController;

import java.util.function.Function;

public class EnumControllerBuilder<E extends Enum<E>> extends AbstractControllerBuilder<E> implements IEnumControllerBuilder<E> {
    private Class<E> enumClass;
    private IValueFormatter<E> formatter = null;

    public EnumControllerBuilder(IOption<E> option) {
        super(option);
    }

    @Override
    public IEnumControllerBuilder<E> enumClass(Class<E> enumClass) {
        this.enumClass = enumClass;
        return this;
    }

    @Override
    public IEnumControllerBuilder<E> formatValue(IValueFormatter<E> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<E> build() {
        IValueFormatter<E> formatter = this.formatter;
        if (formatter == null) {
            Function<E, Component> formatFunction = EnumController.getDefaultFormatter();
            formatter = formatFunction::apply;
        }

        return EnumController.createInternal(option, formatter, enumClass.getEnumConstants());
    }
}
