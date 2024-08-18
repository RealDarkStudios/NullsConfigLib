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

package net.nullved.nullsconfiglib.config;

import net.nullved.nullsconfiglib.api.config.*;
import net.nullved.nullsconfiglib.api.config.autogen.CategorySettings;
import net.nullved.nullsconfiglib.api.config.autogen.ICategorySettings;
import net.nullved.nullsconfiglib.api.config.autogen.Comment;
import net.nullved.nullsconfiglib.api.config.autogen.MutliLineComment;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConfigField<T> implements IConfigField<T> {
    private ReflectionFieldAccess<T> field;
    private final ReflectionFieldAccess<T> defaultField;
    private final IConfigHandler<?> parent;
    private final Optional<ISerialConfigField> serial;
    private final Optional<ICategorySettings> categorySettings;

    public ConfigField(ReflectionFieldAccess<T> field, ReflectionFieldAccess<T> defaultField, IConfigHandler<?> parent,
                       @Nullable ConfigEntry configEntry, @Nullable CategorySettings categorySettings) {
        this.field = field;
        this.defaultField = defaultField;
        this.parent = parent;

        boolean hasComment = field.getAnnotation(Comment.class).isPresent() || field.getAnnotation(MutliLineComment.class).isPresent();

        Optional<List<Comment>> comments;

        if (hasComment) {
            if (field.getAnnotation(Comment.class).isPresent()) {
                comments = Optional.of(Collections.singletonList(field.getAnnotation(Comment.class).get()));
            } else if (field.getAnnotation(MutliLineComment.class).isPresent()) {
                comments = Optional.of(Arrays.asList(field.getAnnotation(MutliLineComment.class).get().comments()));
            } else comments = Optional.empty();
        } else comments = Optional.empty();

        this.serial = configEntry != null ? Optional.of(new SerialConfigFieldImpl(
                "".equals(configEntry.name()) ? field.name() : configEntry.name(),
                comments,
                configEntry.required(),
                configEntry.nullable()
        )) : Optional.empty();

        this.categorySettings = categorySettings != null ? Optional.of(new CategorySettingsFieldImpl<>(
                categorySettings.category(),
                "".equals(categorySettings.group()) ? Optional.empty() : Optional.of(categorySettings.group())
        )) : Optional.empty();
    }

    @Override
    public ReflectionFieldAccess<T> access() {
        return field;
    }

    public void setFieldAccess(ReflectionFieldAccess<T> field) {
        this.field = field;
    }

    @Override
    public IReadOnlyFieldAccess<T> defaultAccess() {
        return defaultField;
    }

    @Override
    public IConfigHandler<?> parent() {
        return parent;
    }

    @Override
    public Optional<ISerialConfigField> serial() {
        return serial;
    }

    @Override
    public Optional<ICategorySettings> categorySettings() {
        return categorySettings;
    }

    private record SerialConfigFieldImpl(String serialName, Optional<List<Comment>> comment, boolean required, boolean nullable) implements ISerialConfigField {
    }

    private record CategorySettingsFieldImpl<T>(String category, Optional<String> group) implements ICategorySettings {
    }
}
