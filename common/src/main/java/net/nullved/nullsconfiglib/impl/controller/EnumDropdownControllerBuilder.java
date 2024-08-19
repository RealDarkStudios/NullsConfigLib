package net.nullved.nullsconfiglib.impl.controller;

import net.nullved.nullsconfiglib.api.IController;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.controller.IEnumDropdownControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;
import net.nullved.nullsconfiglib.gui.controllers.cycling.EnumController;
import net.nullved.nullsconfiglib.gui.controllers.dropdown.EnumDropdownController;

public class EnumDropdownControllerBuilder<E extends Enum<E>> extends AbstractControllerBuilder<E> implements IEnumDropdownControllerBuilder<E> {
    private IValueFormatter<E> formatter = EnumController.<E>getDefaultFormatter()::apply;

    public EnumDropdownControllerBuilder(IOption<E> option) {
        super(option);
    }

    @Override
    public IEnumDropdownControllerBuilder<E> formatValue(IValueFormatter<E> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public IController<E> build() {
        return new EnumDropdownController<>(option, formatter);
    }
}
