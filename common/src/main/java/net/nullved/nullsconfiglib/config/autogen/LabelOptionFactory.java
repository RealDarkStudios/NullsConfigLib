package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.ILabelOption;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionFactory;
import net.nullved.nullsconfiglib.api.config.autogen.Label;

import java.util.List;

public class LabelOptionFactory implements IOptionFactory<Label, Component> {
    @Override
    public IOption<Component> createOption(Label annotation, IConfigField<Component> field, IOptionAccess optionAccess, List<IOptionFlag> flags) {
        return ILabelOption.label(field.access().get());
    }
}
