package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IListOption;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionDescription;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionFactory;
import net.nullved.nullsconfiglib.api.config.autogen.ListGroup;
import net.nullved.nullsconfiglib.config.FieldBackedBinding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ListGroupOptionFactory<T> implements IOptionFactory<ListGroup, List<T>> {
    @Override
    public IOption<List<T>> createOption(ListGroup annotation, IConfigField<List<T>> field, IOptionAccess optionAccess, List<IOptionFlag> flags) {
        if (field.categorySettings().orElseThrow().group().isPresent()) {
            throw new NCLAutoGenException("@ListGroup fields ('%s') cannot be inside a group as lists act as groups.".formatted(field.access().name()));
        }

        ListGroup.IValueFactory<T> valueFactory = createValueFactory((Class<? extends ListGroup.IValueFactory<T>>) annotation.valueFactory());
        ListGroup.IControllerFactory<T> controllerFactory = createControllerFactory((Class<? extends ListGroup.IControllerFactory<T>>) annotation.controllerFactory());

        return IListOption.<T>createBuilder()
                .name(Component.translatable(this.getTranslationKey(field, null)))
                .description(this.description(field))
                .initial(valueFactory::provideNewValue)
                .controller(opt -> controllerFactory.createController(annotation, field, optionAccess, opt))
                .binding(new FieldBackedBinding<>(field.access(), field.defaultAccess()))
                .minNumberOfEntries(annotation.minEntries())
                .maxNumberOfEntries(annotation.maxEntries() == 0 ? Integer.MAX_VALUE : annotation.maxEntries())
                .insertEntriesAtEnd(annotation.addEntriesToBottom())
                .build();
    }

    private IOptionDescription description(IConfigField<List<T>> field) {
        IOptionDescription.Builder builder = IOptionDescription.createBuilder();

        String key = this.getTranslationKey(field, "desc");
        if (Language.getInstance().has(key)) {
            builder.text(Component.translatable(key));
        } else {
            key += ".";
            int i = 0;
            while (Language.getInstance().has(key + i++)) {
                builder.text(Component.translatable(key + i));
            }
        }

        return builder.build();
    }

    private ListGroup.IValueFactory<T> createValueFactory(Class<? extends ListGroup.IValueFactory<T>> clazz) {
        Constructor<? extends ListGroup.IValueFactory<T>> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new NCLAutoGenException("Could not find no-args constructor for `valueFactory` on '%s' for @ListGroup field.".formatted(clazz.getName()), e);
        }

        try {
            return constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NCLAutoGenException("Couldn't invoke no-args constructor for `valueFactory` on '%s' for @ListGroup field.".formatted(clazz.getName()), e);
        }
    }

    private ListGroup.IControllerFactory<T> createControllerFactory(Class<? extends ListGroup.IControllerFactory<T>> clazz) {
        Constructor<? extends ListGroup.IControllerFactory<T>> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new NCLAutoGenException("Could not find no-args constructor on `controllerFactory`, '%s' for @ListGroup field.".formatted(clazz.getName()), e);
        }

        try {
            return constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NCLAutoGenException("Couldn't invoke no-args constructor on `controllerFactory`, '%s' for @ListGroup field.".formatted(clazz.getName()), e);
        }
    }

    private String getTranslationKey(IConfigField<List<T>> field, @Nullable String suffix) {
        String key = "config.%s.%s.%s".formatted(field.parent().id().getNamespace(), field.parent().id().getPath(), field.access().name());
        if (suffix != null) key += "." + suffix;
        return key;
    }
}
