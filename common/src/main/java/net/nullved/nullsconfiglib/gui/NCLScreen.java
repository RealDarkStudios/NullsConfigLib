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

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IConfigCategory;
import net.nullved.nullsconfiglib.api.INCLGui;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionFlag;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.api.utils.MutableDimension;
import net.nullved.nullsconfiglib.api.utils.OptionUtils;
import net.nullved.nullsconfiglib.gui.controllers.ControllerPopupWidget;
import net.nullved.nullsconfiglib.gui.controllers.PopupControllerScreen;
import net.nullved.nullsconfiglib.gui.tab.ListHolderWidget;
import net.nullved.nullsconfiglib.gui.tab.ScrollableNavigationBar;
import net.nullved.nullsconfiglib.gui.tab.TabExt;
import net.nullved.nullsconfiglib.gui.utils.GuiUtils;
import net.nullved.nullsconfiglib.platform.NCLPlatform;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class NCLScreen extends Screen {
    public final INCLGui gui;

    private final Screen parent;

    public final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    public ScrollableNavigationBar tabNavigationBar;
    public ScreenRectangle tabArea;

    public Component saveButtonMessage;
    public Tooltip saveButtonTooltipMessage;
    private int saveButtonMessageTime;

    private boolean pendingChanges, pendingChangesFlags;

    public ControllerPopupWidget<?> currentPopupController = null;
    public boolean popupControllerVisible = false;

    public NCLScreen(INCLGui gui, Screen parent) {
        super(gui.title());
        this.gui = gui;
        this.parent = parent;

        OptionUtils.forEachOptions(gui, option -> {
            option.addListener((opt, val) -> onOptionChanged(opt));
        });
    }

    @Override
    protected void init() {
        tabArea = new ScreenRectangle(0, 24 - 1, this.width, this.height - 24 + 1);

        int currentTab = tabNavigationBar != null ? tabNavigationBar.getTabs().indexOf(tabManager.getCurrentTab()) : 0;
        if (currentTab == -1) currentTab = 0;

        tabNavigationBar = new ScrollableNavigationBar(this.width, tabManager, gui.categories()
                .stream()
                .map(category -> new CategoryTab(this, category, tabArea))
                .toList());

        tabNavigationBar.selectTab(currentTab, false);
        tabNavigationBar.arrangeElements();
        tabManager.setTabArea(tabArea);
        addRenderableWidget(tabNavigationBar);

        gui.initConsumer().accept(this);
    }

    public void addPopupControllerWidget(ControllerPopupWidget<?> controllerPopupWidget) {
        if (currentPopupController != null) {
            clearPopupControllerWidget();
        }

        currentPopupController = controllerPopupWidget;
        popupControllerVisible = true;

        OptionListWidget optionListWidget = null;
        if(this.tabNavigationBar.getTabManager().getCurrentTab() instanceof CategoryTab categoryTab) {
            optionListWidget = categoryTab.optionList.getList();
        }
        if(optionListWidget != null) {
            this.minecraft.setScreen(new PopupControllerScreen(this, controllerPopupWidget));
        }
    }

    public void clearPopupControllerWidget() {
        if(Minecraft.getInstance().screen instanceof PopupControllerScreen popupControllerScreen) {
            popupControllerScreen.onClose();
        }

        popupControllerVisible = false;
        currentPopupController = null;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        if (tabManager.getCurrentTab() instanceof TabExt tab) {
            tab.renderBackground(guiGraphics);
        }
    }

    public void finishOrSave() {
        saveButtonMessage = null;

        if (pendingChanges()) {
            Set<IOptionFlag> flags = new HashSet<>();
            OptionUtils.forEachOptions(gui, option -> {
                if (option.applyValue()) {
                    flags.addAll(option.flags());
                }
            });
            OptionUtils.forEachOptions(gui, option -> {
                if (option.changed()) {
                    // if still changed after applying, reset to the current value from binding
                    // as something has gone wrong.
                    option.forgetPendingValue();
                    NullsConfigLib.LOGGER.error("Option '{}' value mismatch after applying! Reset to binding's getter.", option.name().getString());
                }
            });
            gui.saveFunction().run();

            flags.forEach(flag -> flag.accept(minecraft));

            pendingChanges = false;
            pendingChangesFlags = false;
            if (tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
                categoryTab.updateButtons();
            }
        } else onClose();
    }

    public void cancelOrReset() {
        if (pendingChanges()) { // if pending changes, button acts as a cancel button
            OptionUtils.forEachOptions(gui, IOption::forgetPendingValue);
            onClose();
        } else { // if not, button acts as a reset button
            OptionUtils.forEachOptions(gui, IOption::requestSetDefault);
        }
    }

    public void undo() {
        OptionUtils.forEachOptions(gui, IOption::forgetPendingValue);
    }

    @Override
    public void tick() {
        if (tabManager.getCurrentTab() instanceof TabExt tabExt) {
            tabExt.tick();
        }

        if (tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
            if (saveButtonMessage != null) {
                if (saveButtonMessageTime > 140) {
                    saveButtonMessage = null;
                    saveButtonTooltipMessage = null;
                    saveButtonMessageTime = 0;
                } else {
                    saveButtonMessageTime++;
                    categoryTab.saveFinishedButton.setMessage(saveButtonMessage);
                    if (saveButtonTooltipMessage != null) {
                        categoryTab.saveFinishedButton.setTooltip(saveButtonTooltipMessage);
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            this.setDragging(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.getFocused() != null
                && this.isDragging()
                && (button == InputConstants.MOUSE_BUTTON_LEFT || button == InputConstants.MOUSE_BUTTON_RIGHT)
                && this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public void setSaveButtonMessage(Component message, Component tooltip) {
        saveButtonMessage = message;
        saveButtonTooltipMessage = Tooltip.create(tooltip);
        saveButtonMessageTime = 0;
    }

    public boolean pendingChanges() {
        return pendingChanges;
    }

    public boolean pendingChangesHaveFlags() {
        return pendingChangesFlags;
    }

    private void onOptionChanged(IOption<?> option) {
        pendingChanges = false;
        pendingChangesFlags = false;

        OptionUtils.consumeOptions(gui, opt -> {
            pendingChanges |= opt.changed();
            pendingChangesFlags |= opt.changed() && !opt.flags().isEmpty();
            return pendingChangesFlags;
        });

        if (tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
            categoryTab.updateButtons();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if (pendingChanges()) {
            setSaveButtonMessage(Component.translatable("ncl.gui.save_before_exit").withStyle(ChatFormatting.RED), Component.translatable("ncl.gui.save_before_exit.tooltip"));
            return false;
        }
        return true;
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    public static void renderMultilineTooltip(GuiGraphics graphics, Font font, MultiLineLabel text, int centerX, int yAbove, int yBelow, int screenWidth, int screenHeight) {
        if (text.getLineCount() > 0) {
            int maxWidth = text.getWidth();
            int lineHeight = font.lineHeight + 1;
            int height = text.getLineCount() * lineHeight - 1;

            int belowY = yBelow + 12;
            int aboveY = yAbove - height + 12;
            int maxBelow = screenHeight - (belowY + height);
            int minAbove = aboveY - height;
            int y = aboveY;
            if (minAbove < 8) y = maxBelow > minAbove ? belowY : aboveY;

            int x = Math.max(centerX - text.getWidth() / 2 - 12, -6);

            int drawX = x + 12;
            int drawY = y - 12;

            graphics.pose().pushPose();
            TooltipRenderUtil.renderTooltipBackground(
                    graphics,
                    drawX,
                    drawY,
                    maxWidth,
                    height,
                    400
            );
            graphics.pose().translate(0.0, 0.0, 400.0);

            text.renderLeftAligned(graphics, drawX, drawY, lineHeight, -1);

            graphics.pose().popPose();
        }
    }

    public static class CategoryTab implements TabExt {
        private static final ResourceLocation DARKER_BG = NCLPlatform.mcRl("textures/gui/menu_list_background.png");

        private final NCLScreen screen;
        private final IConfigCategory category;
        private final Tooltip tooltip;

        private ListHolderWidget<OptionListWidget> optionList;
        public final Button saveFinishedButton;
        public final Button cancelResetButton;
        public final Button undoButton;
        private final SearchFieldWidget searchField;
        private OptionDescriptionWidget descriptionWidget;

        private final ScreenRectangle rightPaneDim;

        public CategoryTab(NCLScreen screen, IConfigCategory category, ScreenRectangle tabArea) {
            this.screen = screen;
            this.category = category;
            this.tooltip = Tooltip.create(category.tooltip());

            int columnWidth = screen.width / 3;
            int padding = columnWidth / 20;
            columnWidth = Math.min(columnWidth, 400);
            int paddedWidth = columnWidth - padding * 2;
            rightPaneDim = new ScreenRectangle(screen.width / 3 * 2, tabArea.top() + 1, screen.width / 3, tabArea.height());
            MutableDimension<Integer> actionDim = Dimension.ofInt(screen.width / 3 * 2 + screen.width / 6, screen.height - padding - 20, paddedWidth, 20);

            saveFinishedButton = Button.builder(Component.literal("Done"), btn -> screen.finishOrSave())
                    .pos(actionDim.x() - actionDim.width() / 2, actionDim.y())
                    .size(actionDim.width(), actionDim.height())
                    .build();

            actionDim.expand(-actionDim.width() / 2 - 2, 0).move(-actionDim.width() / 2 - 2, -22);
            cancelResetButton = Button.builder(Component.literal("Cancel"), btn -> screen.cancelOrReset())
                    .pos(actionDim.x() - actionDim.width() / 2, actionDim.y())
                    .size(actionDim.width(), actionDim.height())
                    .build();

            actionDim.move(actionDim.width() + 4, 0);
            undoButton = Button.builder(Component.translatable("ncl.gui.undo"), btn -> screen.undo())
                    .pos(actionDim.x() - actionDim.width() / 2, actionDim.y())
                    .size(actionDim.width(), actionDim.height())
                    .tooltip(Tooltip.create(Component.translatable("ncl.gui.undo.tooltip")))
                    .build();

            searchField = new SearchFieldWidget(
                    screen,
                    screen.font,
                    screen.width / 3 * 2 + screen.width / 6 - paddedWidth / 2 + 1,
                    undoButton.getY() - 22,
                    paddedWidth - 2, 18,
                    Component.translatable("gui.recipebook.search_hint"),
                    Component.translatable("gui.recipebook.search_hint"),
                    searchQuery -> optionList.getList().updateSearchQuery(searchQuery)
            );

            this.optionList = new ListHolderWidget<>(
                    () -> new ScreenRectangle(tabArea.position(), tabArea.width() / 3 * 2, tabArea.height()),
                    new OptionListWidget(screen, category, screen.minecraft, 0, 0, screen.width / 3 * 2 + 1, screen.height, desc -> {
                        descriptionWidget.setOptionDescription(desc);
                    })
            );

            descriptionWidget = new OptionDescriptionWidget(
                    () -> new ScreenRectangle(
                            screen.width / 3 * 2 + padding,
                            tabArea.top() + padding,
                            paddedWidth,
                            searchField.getY() - 1 - tabArea.top() - padding * 2
                    ),
                    null
            );

            updateButtons();
        }

        @Override
        public Component getTabTitle() {
            return category.name();
        }

        @Override
        public void visitChildren(Consumer<AbstractWidget> consumer) {
            consumer.accept(optionList);
            consumer.accept(saveFinishedButton);
            consumer.accept(cancelResetButton);
            consumer.accept(undoButton);
            consumer.accept(searchField);
            consumer.accept(descriptionWidget);
        }

        @Override
        public void renderBackground(GuiGraphics graphics) {
            RenderSystem.enableBlend();
            // right pane darker db
            graphics.blit(DARKER_BG, rightPaneDim.left(), rightPaneDim.top(), rightPaneDim.right() + 2, rightPaneDim.bottom() + 2, rightPaneDim.width() + 2, rightPaneDim.height() + 2, 32, 32);

            // top separator for right pane
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 10);
            graphics.blit(CreateWorldScreen.HEADER_SEPARATOR, rightPaneDim.left() - 1, rightPaneDim.top() - 2, 0.0F, 0.0F, rightPaneDim.width() + 1, 2, 32, 2);
            graphics.pose().popPose();

            // left separator for right pane
            graphics.pose().pushPose();
            graphics.pose().translate(rightPaneDim.left(), rightPaneDim.top() - 1, 0);
            graphics.pose().rotateAround(Axis.ZP.rotationDegrees(90), 0, 0, 1);
            graphics.blit(CreateWorldScreen.FOOTER_SEPARATOR, 0, 0, 0f, 0f, rightPaneDim.height() + 1, 2, 32, 2);
            graphics.pose().popPose();

            RenderSystem.disableBlend();
        }

        @Override
        public void doLayout(ScreenRectangle screenRectangle) {
        }

        @Override
        public void tick() {
            descriptionWidget.tick();
        }

        @Override
        public @NotNull Tooltip getTooltip() {
            return tooltip;
        }

        public void updateButtons() {
            boolean pendingChanges = screen.pendingChanges();
            boolean flags = screen.pendingChangesHaveFlags();

            undoButton.active = pendingChanges;
            saveFinishedButton.setMessage(pendingChanges ? Component.translatable("ncl.gui.save").withStyle(flags ? ChatFormatting.RED : ChatFormatting.RESET) : GuiUtils.translatableFallback("ncl.gui.done", CommonComponents.GUI_DONE));
            saveFinishedButton.setTooltip(new NCLTooltip(pendingChanges ? Component.translatable("ncl.gui.save.tooltip") : Component.translatable("ncl.gui.finished.tooltip"), saveFinishedButton));
            cancelResetButton.setMessage(pendingChanges ? GuiUtils.translatableFallback("ncl.gui.cancel", CommonComponents.GUI_CANCEL) : Component.translatable("controls.reset"));
            cancelResetButton.setTooltip(new NCLTooltip(pendingChanges ? Component.translatable("ncl.gui.cancel.tooltip") : Component.translatable("ncl.gui.reset.tooltip"), cancelResetButton));
        }
    }
}
