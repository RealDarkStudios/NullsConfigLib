package net.nullved.nullsconfiglib.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TabNavigationBar.class)
public interface TabNavigationBarAccessor {
    /**
     * Accessor for {@link TabNavigationBar#layout}
     * @return The {@link LinearLayout}
     */
    @Accessor("layout")
    LinearLayout ncl$getLayout();

    /**
     * Accessor for {@link TabNavigationBar#width}
     * @return The {@code int}
     */
    @Accessor("width")
    int ncl$getWidth();

    /**
     * Accessor for {@link TabNavigationBar#tabManager}
     * @return The {@link TabManager}
     */
    @Accessor("tabManager")
    TabManager ncl$getTabManager();

    /**
     * Accessor for {@link TabNavigationBar#tabs}
     * @return The {@link ImmutableList<Tab>}
     */
    @Accessor("tabs")
    ImmutableList<Tab> ncl$getTabs();

    /**
     * Accessor for {@link TabNavigationBar#tabButtons}
     * @return The {@link ImmutableList<TabButton>}
     */
    @Accessor("tabButtons")
    ImmutableList<TabButton> ncl$getTabButtons();
}
