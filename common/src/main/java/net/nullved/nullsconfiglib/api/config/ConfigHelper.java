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

package net.nullved.nullsconfiglib.api.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.nullved.nullsconfiglib.config.autogen.NCLAutoGenException;

import java.lang.reflect.Field;
import java.util.function.Function;

public class ConfigHelper<O> {
    private final Class<O> clazz;

    public ConfigHelper(Class<O> clazz) {
        this.clazz = clazz;
    }

    public <T> RecordCodecBuilder<O, T> codecBuilder(Codec<T> codec, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            return codec.fieldOf(field.getName()).forGetter(c -> {
                try {
                    return (T) field.get(c);
                } catch (IllegalAccessException e) {
                    throw new NCLAutoGenException("Failed to get value for codec! Field name: '%s' in config '%s'".formatted(fieldName, clazz.getName()), e);
                }
            });
        } catch (NoSuchFieldException e) {
            throw new NCLAutoGenException("Field '%s' does not exist in config class '%s'".formatted(fieldName, clazz.getName()), e);
        }
    }

    public <T> RecordCodecBuilder<O, T> codecBuilder(MapCodec<T> codec, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            return codec.fieldOf(field.getName()).forGetter(c -> {
                try {
                    return (T) field.get(c);
                } catch (IllegalAccessException e) {
                    throw new NCLAutoGenException("Failed to get value for codec! Field name: '%s' in config '%s'".formatted(fieldName, clazz.getName()), e);
                }
            });
        } catch (NoSuchFieldException e) {
            throw new NCLAutoGenException("Field '%s' does not exist in config class '%s'".formatted(fieldName, clazz.getName()), e);
        }
    }

    public ConfigFunction<O, Boolean> packetCodecGetter(String fieldName) {
        try {
            return new ConfigFunction<>(clazz.getField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new NCLAutoGenException("Unable to get field '%s' for config '%s'".formatted(fieldName, clazz.getName()), e);
        }
    }

    public static class ConfigFunction<I, O> implements Function<I, O> {
        private final Field field;

        public ConfigFunction(Field field) {
            this.field = field;
        }

        @Override
        public O apply(I i) {
            try {
                return (O) field.get(i);
            } catch (IllegalAccessException e) {
                throw new NCLAutoGenException("error", e);
            }
        }
    }
}
