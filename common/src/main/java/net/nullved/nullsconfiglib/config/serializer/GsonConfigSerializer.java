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

package net.nullved.nullsconfiglib.config.serializer;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import dev.architectury.platform.Platform;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.config.*;
import net.nullved.nullsconfiglib.api.config.autogen.Comment;
import net.nullved.nullsconfiglib.api.config.serializer.IGsonConfigSerializerBuilder;
import net.nullved.nullsconfiglib.gui.utils.ItemRegistryHelper;
import net.nullved.nullsconfiglib.platform.NCLPlatform;
import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.parsers.json.JsonReader;
import org.quiltmc.parsers.json.JsonWriter;
import org.quiltmc.parsers.json.gson.GsonReader;
import org.quiltmc.parsers.json.gson.GsonWriter;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class GsonConfigSerializer<T> extends IConfigSerializer<T> {
    private final Gson gson;
    private final Path path;
    private final boolean json5;

    private GsonConfigSerializer(IConfigHandler<T> config, Gson gson, Path path, boolean json5) {
        super(config);
        this.gson = gson;
        this.path = path;
        this.json5 = json5;
    }

    @Override
    public void save() {
        NullsConfigLib.LOGGER.info("Serializing {} to '{}'", config.configClass(), path);

        try (StringWriter stringWriter = new StringWriter()) {
            JsonWriter jsonWriter = json5 ? JsonWriter.json5(stringWriter) : JsonWriter.json(stringWriter);
            GsonWriter gsonWriter = new GsonWriter(jsonWriter);

            jsonWriter.beginObject();

            for (IConfigField<?> field: config.fields()) {
                ISerialConfigField serial = field.serial().orElse(null);
                if (serial == null) continue;

                if (!json5 && serial.comment().isPresent() && Platform.isDevelopmentEnvironment()) {
                    NullsConfigLib.LOGGER.warn("Found comment in config field '{}', but json5 isn't enabled. Enable it with `.setJson5(true)` on the `GsonConfigSerializerBuilder`. Comments will not be serialized. This warning is only visible in development environments.", serial.serialName());
                }

                for (Comment comment: serial.comment().isPresent() ? serial.comment().get() : List.<Comment>of()) {
                    jsonWriter.comment(comment.value());
                }

                jsonWriter.name(serial.serialName());

                JsonElement element;
                try {
                    element = gson.toJsonTree(field.access().get(), field.access().type());
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Failed to serialize config field '{}'. Serializing as null", field, e);
                    jsonWriter.nullValue();
                    continue;
                }

                try {
                    gson.toJson(element, gsonWriter);
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Failed to serialize config field '{}' Due to the error state this JSON writer cannot continue safely and the save will be abandoned.", field, e);
                    return;
                }
            }

            jsonWriter.endObject();
            jsonWriter.flush();

            Files.createDirectories(path.getParent());
            Files.writeString(path, stringWriter.toString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            NullsConfigLib.LOGGER.error("Failed to serialize config class '{}'", config.configClass().getSimpleName(), e);
        }
    }

    @Override
    public LoadResult load(Map<IConfigField<?>, IFieldAccess<?>> bufferAccessMap) {
        if (!Files.exists(path)) {
            NullsConfigLib.LOGGER.error("Config file '{}' does not exist. Creating with default values", path);
            save();
            return LoadResult.NO_CHANGE;
        }

        NullsConfigLib.LOGGER.info("Deserializing '{}' from '{}'", config.configClass().getSimpleName(), path);

        Map<String, IConfigField<?>> fieldMap = Arrays.stream(config.fields())
                .filter(field -> field.serial().isPresent())
                .collect(Collectors.toMap(f -> f.serial().orElseThrow().serialName(), Function.identity()));

        Set<String> missingFields = fieldMap.keySet();
        boolean dirty = false;

        try (JsonReader jsonReader = json5 ? JsonReader.json5(path) : JsonReader.json(path)) {
            GsonReader gsonReader = new GsonReader(jsonReader);

            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                IConfigField<?> field = fieldMap.get(name);
                missingFields.remove(name);

                if (field == null) {
                    NullsConfigLib.LOGGER.warn("Found unknown config field '{}'", name);
                    jsonReader.skipValue();
                    continue;
                }

                IFieldAccess<?> bufferAccess = bufferAccessMap.get(field);
                ISerialConfigField serial = field.serial().orElse(null);
                if (serial == null) continue;

                JsonElement element;
                try {
                    element = gson.fromJson(gsonReader, JsonElement.class);
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Failed to deserialize config field '{}'. Due to the error state this JSON reader cannot be re-used and loading will be aborted.", name, e);
                    return LoadResult.FAILURE;
                }

                if (element.isJsonNull() && !serial.nullable()) {
                    NullsConfigLib.LOGGER.warn("Found null value in non-nullable ocnfig field '{}'. Leaving field as default and marking as dirty", name);
                    dirty = true;
                    continue;
                }

                try {
                    bufferAccess.set(gson.fromJson(element, bufferAccess.type()));
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Failed to deserialize config field '{}'. Leaving as default.", name, e);
                }
            }

            jsonReader.endObject();
        } catch (IOException e) {
            NullsConfigLib.LOGGER.error("Failed to deserialize config class '{}'", config.configClass().getSimpleName(), e);
            return LoadResult.FAILURE;
        }

        if (!missingFields.isEmpty()) {
            for (String missingField : missingFields) {
                if (fieldMap.get(missingField).serial().get().required()) {
                    dirty = true;
                    NullsConfigLib.LOGGER.warn("Missing required config field '{}'. Resaving as default", missingField);
                }
            }
        }

        return dirty ? LoadResult.DIRTY : LoadResult.SUCCESS;
    }

    public static class StyleTypeAdapter implements JsonSerializer<Style>, JsonDeserializer<Style> {
        @Override
        public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Style.Serializer.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(Style.EMPTY);
        }

        @Override
        public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
            return Style.Serializer.CODEC.encodeStart(JsonOps.INSTANCE, src).result().orElse(JsonNull.INSTANCE);
        }
    }

    public static class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Color(jsonElement.getAsInt(), true);
        }

        @Override
        public JsonElement serialize(Color color, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(color.getRGB());
        }
    }

    public static class ItemTypeAdapter implements JsonSerializer<Item>, JsonDeserializer<Item> {
        @Override
        public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ItemRegistryHelper.getItemFromName(jsonElement.getAsString());
        }

        @Override
        public JsonElement serialize(Item item, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(BuiltInRegistries.ITEM.getKey(item).toString());
        }
    }

    @ApiStatus.Internal
    public static class Builder<T> implements IGsonConfigSerializerBuilder<T> {
        private final IConfigHandler<T> config;
        private Path path;
        private boolean json5;
        private UnaryOperator<GsonBuilder> gsonBuilder = builder -> builder
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                /*? if >1.20.4 {*/
                .registerTypeHierarchyAdapter(Component.class, new Component.SerializerAdapter(RegistryAccess.EMPTY))
                /*?} elif =1.20.4 {*/
                /*.registerTypeHierarchyAdapter(Component.class, new Component.SerializerAdapter())
                 *//*?} else {*/
                /*.registerTypeHierarchyAdapter(Component.class, new Component.Serializer())
                 *//*?}*/
                .registerTypeHierarchyAdapter(Style.class, /*? if >=1.20.4 {*/new StyleTypeAdapter()/*?} else {*//*new Style.Serializer()*//*?}*/)
                .registerTypeHierarchyAdapter(Color.class, new ColorTypeAdapter())
                .registerTypeHierarchyAdapter(Item.class, new ItemTypeAdapter())
                .setPrettyPrinting();

        public Builder(IConfigHandler<T> config) {
            this.config = config;
        }

        @Override
        public Builder<T> setPath(Path path) {
            this.path = path;
            return this;
        }

        @Override
        public Builder<T> overrideGsonBuilder(GsonBuilder gsonBuilder) {
            this.gsonBuilder = builder -> gsonBuilder;
            return this;
        }

        @Override
        public Builder<T> overrideGsonBuilder(Gson gson) {
            return this.overrideGsonBuilder(gson.newBuilder());
        }

        @Override
        public Builder<T> appendGsonBuilder(UnaryOperator<GsonBuilder> gsonBuilder) {
            UnaryOperator<GsonBuilder> prev = this.gsonBuilder;
            this.gsonBuilder = builder -> gsonBuilder.apply(prev.apply(builder));
            return this;
        }

        @Override
        public Builder<T> setJson5(boolean json5) {
            this.json5 = json5;
            return this;
        }

        @Override
        public GsonConfigSerializer<T> build() {
            return new GsonConfigSerializer<>(config, gsonBuilder.apply(new GsonBuilder()).create(), path, json5);
        }
    }
}
