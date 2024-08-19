package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.*;
import net.nullved.nullsconfiglib.api.config.autogen.Boolean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class OptionFactoryRegistry {
    private static final Map<Class<?>, IOptionFactory<?, ?>> factoryMap = new HashMap<>();

    static {
        registerOptionFactory(Boolean.class, new BooleanOptionFactory());
        registerOptionFactory(ColorField.class, new ColorFieldOptionFactory());
        registerOptionFactory(DoubleField.class, new DoubleFieldOptionFactory());
        registerOptionFactory(DoubleSlider.class, new DoubleSliderOptionFactory());
        registerOptionFactory(EnumCycler.class, new EnumCyclerOptionFactory());
        registerOptionFactory(FloatField.class, new FloatFieldOptionFactory());
        registerOptionFactory(FloatSlider.class, new FloatSliderOptionFactory());
        registerOptionFactory(IntField.class, new IntFieldOptionFactory());
        registerOptionFactory(IntSlider.class, new IntSliderOptionFactory());
        registerOptionFactory(ItemField.class, new ItemFieldOptionFactory());
        registerOptionFactory(Label.class, new LabelOptionFactory());
        registerOptionFactory(ListGroup.class, new ListGroupOptionFactory<>());
        registerOptionFactory(LongField.class, new LongFieldOptionFactory());
        registerOptionFactory(LongSlider.class, new LongSliderOptionFactory());
        registerOptionFactory(MasterTickBox.class, new MasterTickBoxOptionFactory());
        registerOptionFactory(StringField.class, new StringFieldOptionFactory());
        registerOptionFactory(TickBox.class, new TickBoxOptionFactory());
    }

    public static <A extends Annotation, T> void registerOptionFactory(Class<A> annotation, IOptionFactory<A, T> factory) {
        factoryMap.put(annotation, factory);
    }

    public static <T> Optional<IOption<T>> createOption(Field field, IConfigField<T> configField, IOptionAccess storage) {
        Annotation[] annotations = Arrays.stream(field.getAnnotations())
                .filter(annotation -> factoryMap.containsKey(annotation.annotationType()))
                .toArray(Annotation[]::new);

        if (annotations.length != 1) {
            NullsConfigLib.LOGGER.warn("Found {} option factory annotations on field {}, expected 1", annotations.length, field);

            if (annotations.length == 0) {
                return Optional.empty();
            }
        }

        List<IOptionFlag> flags = OptionFlagRegistry.getFlags(field, configField, storage);

        Annotation annotation = annotations[0];
        // noinspection unchecked
        IOptionFactory<Annotation, T> factory = (IOptionFactory<Annotation, T>) factoryMap.get(annotation.annotationType());
        return Optional.of(factory.createOption(annotation, configField, storage, flags));
    }
}
