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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UndoRedoHelper {
    private final List<FieldState> history = new ArrayList<>();
    private int index = 0;

    public UndoRedoHelper(String text, int cursorPos, int selectionLength) {
        history.add(new FieldState(text, cursorPos, selectionLength));
    }

    public void save(String text, int cursorPos, int selectionLength) {
        int max = history.size();
        history.subList(index, max).clear();
        history.add(new FieldState(text, cursorPos, selectionLength));
        index++;
    }

    public @Nullable FieldState undo() {
        index--;
        index = Math.max(index, 0);

        if (history.isEmpty()) return null;
        return history.get(index);
    }

    public @Nullable FieldState redo() {
        if (index < history.size() - 1) {
            index++;
            return history.get(index);
        } else return null;
    }

    public record FieldState(String text, int cursorPos, int selectionLength) {}
}
