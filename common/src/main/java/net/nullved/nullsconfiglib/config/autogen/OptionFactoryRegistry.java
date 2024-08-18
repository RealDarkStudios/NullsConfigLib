/*
 * NullsConfigLib - A Config Library for Null's Mods
 * Copyright (C) 2024 NullVed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
