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

package net.nullved.nullsconfiglib.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class SearchFieldWidget extends EditBox {
    private Component emptyText;
    private final NCLScreen nclScreen;
    private final Font font;
    private final Consumer<String> updateConsumer;

    private boolean isEmpty = true;

    public SearchFieldWidget(NCLScreen nclScreen, Font font, int x, int y, int width, int height, Component text,
                             Component emptyText, Consumer<String> updateConsumer) {
        super(font, x, y, width, height, text);
        setResponder(this::update);
        setFilter(string -> !string.endsWith(" ") && !string.startsWith(" "));
        this.nclScreen = nclScreen;
        this.font = font;
        this.emptyText = emptyText;
        this.updateConsumer = updateConsumer;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        if (isVisible() && isEmpty) {
            guiGraphics.drawString(font, emptyText, getX() + 4, getY() + (height - 8) / 2, 0x707070, true);
        }
    }

    private void update(String query) {
        boolean wasEmpty = isEmpty;
        isEmpty = query.isEmpty();

        if (isEmpty && wasEmpty)
            return;

        updateConsumer.accept(query);
    }

    public String getQuery() {
        return getValue().toLowerCase();
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Component getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(Component emptyText) {
        this.emptyText = emptyText;
    }
}
