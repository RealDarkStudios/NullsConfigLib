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

import java.util.Map;

public abstract class IConfigSerializer<T> {
    protected final IConfigHandler<T> config;

    public IConfigSerializer(IConfigHandler<T> config) {
        this.config = config;
    }

    public abstract void save();

    public LoadResult load(Map<IConfigField<?>, IFieldAccess<?>> bufferAccessMap) {
        return LoadResult.NO_CHANGE;
    }

    public enum LoadResult {
        /**
         * Indicates that the config was loaded successfully and the temporary object should be applied.
         */
        SUCCESS,
        /**
         * Indicates that the config was not loaded successfully and the load should be abandoned.
         */
        FAILURE,
        /**
         * Indicates that the config has not changed after a load and the temporary object should be ignored.
         */
        NO_CHANGE,
        /**
         * Indicates the config was loaded successfully, but the config should be re-saved straight away.
         */
        DIRTY
    }
}
