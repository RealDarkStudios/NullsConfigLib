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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.api.INCLGui;
import net.nullved.nullsconfiglib.config.ConfigHandler;

import java.util.function.Function;

public interface IConfigHandler<T> {
    T instance();
    T defaults();
    StreamCodec<RegistryFriendlyByteBuf, T> codec();
    boolean hasCodec();
    Class<T> configClass();
    IConfigField<?>[] fields();
    ResourceLocation id();
    INCLGui generateGui();
    boolean supportsAutoGen();
    boolean load();
    void save();

    IConfigSerializer<T> serializer();

    static <T> Builder<T> createBuilder(Class<T> configClass) {
        return new ConfigHandler.Builder<>(configClass);
    }

    interface Builder<T> {
        /**
         * The unique identifier of this config handler.
         * The namespace should be your modid.
         *
         * @return this builder
         */
        Builder<T> id(ResourceLocation id);

        Builder<T> codec(StreamCodec<RegistryFriendlyByteBuf, T> codec);

        /**
         * The function to create the serializer for this config class.
         *
         * @return this builder
         */
        Builder<T> serializer(Function<IConfigHandler<T>, IConfigSerializer<T>> serializerFactory);

        IConfigHandler<T> build();
    }
}
