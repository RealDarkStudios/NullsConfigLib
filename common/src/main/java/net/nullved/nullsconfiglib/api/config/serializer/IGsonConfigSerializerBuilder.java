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

package net.nullved.nullsconfiglib.api.config.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.api.config.IConfigSerializer;
import net.nullved.nullsconfiglib.config.serializer.GsonConfigSerializer;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public interface IGsonConfigSerializerBuilder<T> {
    static <T> IGsonConfigSerializerBuilder<T> create(IConfigHandler<T> config) {
        return new GsonConfigSerializer.Builder<>(config);
    }

    IGsonConfigSerializerBuilder<T> setPath(Path path);

    IGsonConfigSerializerBuilder<T> overrideGsonBuilder(GsonBuilder gsonBuilder);

    IGsonConfigSerializerBuilder<T> overrideGsonBuilder(Gson gson);

    IGsonConfigSerializerBuilder<T> appendGsonBuilder(UnaryOperator<GsonBuilder> gsonBuilder);

    IGsonConfigSerializerBuilder<T> setJson5(boolean json5);

    IConfigSerializer<T> build();
}
