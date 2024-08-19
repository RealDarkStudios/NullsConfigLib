package net.nullved.nullsconfiglib.api.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.config.autogen.OptionFactoryRegistry;

import java.lang.annotation.Annotation;
import java.util.List;

public interface IOptionFactory<A extends Annotation, T> {
    IOption<T> createOption(A annotation, IConfigField<T> field, IOptionAccess optionAccess, List<IOptionFlag> flags);

    static <A extends Annotation, T> void register(Class<A> annotation, IOptionFactory<A, T> factory) {
        OptionFactoryRegistry.registerOptionFactory(annotation, factory);
    }
}
