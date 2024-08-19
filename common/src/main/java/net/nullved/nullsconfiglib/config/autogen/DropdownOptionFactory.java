package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.Dropdown;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IDropdownStringControllerBuilder;

public class DropdownOptionFactory extends SimpleOptionFactory<Dropdown, String> {
    @Override
    protected IControllerBuilder<String> createController(Dropdown annotation, IConfigField<String> field, IOptionAccess storage, IOption<String> option) {
        return IDropdownStringControllerBuilder.create(option)
                .values(annotation.values())
                .allowEmptyValue(annotation.allowEmptyValue())
                .allowAnyValue(annotation.allowAnyValue());
    }
}
