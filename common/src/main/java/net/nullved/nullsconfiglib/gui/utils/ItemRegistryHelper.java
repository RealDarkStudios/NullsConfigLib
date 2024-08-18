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

package net.nullved.nullsconfiglib.gui.utils;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.nullved.nullsconfiglib.platform.NCLPlatform;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class ItemRegistryHelper {
    public static boolean isRegisteredItem(String identifier) {
        try {
            ResourceLocation itemIdentifier = NCLPlatform.parseRl(identifier.toLowerCase());
            return BuiltInRegistries.ITEM.containsKey(itemIdentifier);
        } catch (ResourceLocationException e) {
            return false;
        }
    }

    /**
     * Looks up the item of the given identifier string.
     *
     * @param identifier  Item identifier, either of the format "namespace:path" or "path". If no namespace is included,
     *                    the default vanilla namespace "minecraft" is used.
     * @param defaultItem Fallback item that gets returned if the identifier does not name a registered item.
     * @return The item identified by the given string, or the fallback if the identifier is not known.
     */
    public static Item getItemFromName(String identifier, Item defaultItem) {
        try {
            ResourceLocation itemIdentifier = NCLPlatform.parseRl(identifier.toLowerCase());
            if (BuiltInRegistries.ITEM.containsKey(itemIdentifier)) {
                return BuiltInRegistries.ITEM.get(itemIdentifier);
            }
        } catch (ResourceLocationException ignored) {
        }
        return defaultItem;
    }

    /**
     * Looks up the item of the given identifier string.
     *
     * @param identifier Item identifier, either of the format "namespace:path" or "path". If no namespace is included,
     *                   the default vanilla namespace "minecraft" is used.
     * @return The item identified by the given string, or `Items.AIR` if the identifier is not known.
     */
    public static Item getItemFromName(String identifier) {
        return getItemFromName(identifier, Items.AIR);
    }

    /**
     * Returns a list of item identifiers matching the given string. The value matches an identifier if:
     * <ul>
     * <li>No namespace is provided in the value and the value is a substring of the path segment of any identifier,
     * regardless of namespace.</li>
     * <li>A namespace is provided, equals the identifier's namespace, and the value is the begin of the identifier's
     * path segment.</li>
     * </ul>
     *
     * @param value (partial) identifier, either of the format "namespace:path" or "path".
     * @return list of matching item identifiers; empty if the given string does not correspond to any known identifiers
     */
    public static Stream<ResourceLocation> getMatchingItemIdentifiers(String value) {
        int sep = value.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
        Predicate<ResourceLocation> filterPredicate;
        if (sep == -1) {
            filterPredicate = identifier ->
                    identifier.getPath().contains(value)
                            || BuiltInRegistries.ITEM.get(identifier).getDescription().getString().toLowerCase().contains(value.toLowerCase());
        } else {
            String namespace = value.substring(0, sep);
            String path = value.substring(sep + 1);
            filterPredicate = identifier -> identifier.getNamespace().equals(namespace) && identifier.getPath().startsWith(path);
        }
        return BuiltInRegistries.ITEM.holders()
                .map(holder -> holder.key().location())
                .filter(filterPredicate)
                /*
                 Sort items as follows based on the given "value" string's path:
                 - if both items' paths begin with the entered string, sort the identifiers (including namespace)
                 - otherwise, if either of the items' path begins with the entered string, sort it to the left
                 - else neither path matches: sort by identifiers again

                 This allows the user to enter "diamond_ore" and match "minecraft:diamond_ore" before
                 "minecraft:deepslate_diamond_ore", even though the second is lexicographically smaller
                 */
                .sorted((id1, id2) -> {
                    String path = (sep == -1 ? value : value.substring(sep + 1)).toLowerCase();
                    boolean id1StartsWith = id1.getPath().toLowerCase().startsWith(path);
                    boolean id2StartsWith = id2.getPath().toLowerCase().startsWith(path);
                    if (id1StartsWith) {
                        if (id2StartsWith) {
                            return id1.compareTo(id2);
                        }
                        return -1;
                    }
                    if (id2StartsWith) {
                        return 1;
                    }
                    return id1.compareTo(id2);
                });
    }
}
