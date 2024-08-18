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

package net.nullved.nullsconfiglib.neoforge;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.loading.FMLPaths;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.ConfigEntry;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.api.config.autogen.Boolean;
import net.nullved.nullsconfiglib.api.config.autogen.Label;
import net.nullved.nullsconfiglib.api.config.autogen.*;
import net.nullved.nullsconfiglib.api.config.serializer.IGsonConfigSerializerBuilder;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.IStringControllerBuilder;
import net.nullved.nullsconfiglib.platform.NCLPlatform;

import java.awt.*;
import java.util.List;

public class NeoForgeTestConfig {
    public static final IConfigHandler<NeoForgeTestConfig> HANDLER = IConfigHandler.createBuilder(NeoForgeTestConfig.class)
            .id(NCLPlatform.rl("neoforge-test"))
            .serializer(config -> IGsonConfigSerializerBuilder.create(config)
                    .setPath(FMLPaths.CONFIGDIR.get().resolve("neoforge-test.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a boolean")
    @Boolean(formatter = Boolean.Formatter.ON_OFF, colored = true)
    public static boolean tboolean = false;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a tick box")
    @TickBox
    public static boolean tickBox = false;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a color field")
    @ColorField
    public static Color color = Color.RED;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a double field")
    @DoubleField(min = -69D, max = 69D)
    public static double doubleField = 0.69D;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a double slider")
    @DoubleSlider(min = -69D, max = 69D, step = 0.69D)
    public static double doubleSlider = -0.69D;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a enum cycler")
    @EnumCycler
    public static TestEnum tenum = TestEnum.ONE;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a float field")
    @FloatField(min = -69f, max = 69f)
    public static float floatField = 6.9f;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a float slider")
    @FloatSlider(min = -690f, max = 690f, step = 6.9f)
    public static float floatSlider = -6.9f;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a int field")
    @IntField(min = -6900, max = 6900)
    public static int intField = 69;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a int slider")
    @IntSlider(min = -6900, max = 6900, step = 69)
    public static int intSlider = -69;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is an item field")
    @ItemField
    public static Item item = Items.STICK;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a label")
    @Label
    public static Component label = Component.literal("label loll").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED).withBold(true).withItalic(true));

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a list group")
    @ListGroup(valueFactory = TestListValueFactory.class, controllerFactory = TestListControllerFactory.class)
    public static List<String> list = List.of();

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a long field")
    @LongField(min = -6900000L, max = 6900000L)
    public static long longField = 69000L;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a long slider")
    @LongSlider(min = -6900000L, max = 6900000L, step = 69000L)
    public static long longSlider = -69000L;

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a string field")
    @StringField
    public static String string = "hello";

    @ConfigEntry
    @CategorySettings(category = "general")
    @Comment("This is a MASTER tick box")
    @MasterTickBox(fields = {"tboolean", "color", "doubleField", "doubleSlider", "tenum", "floatField", "floatSlider", "intField", "intSlider", "item", "list", "longField", "longSlider", "string"})
    public static boolean masterTickBox = true;

    public enum TestEnum {
        ONE,
        TWO,
        BUCKLE_MY_SHOE,
        THREE,
        FOUR,
        BUCKLE_SOME_MORE,
        FIVE,
        SIX,
        NIKE_KICKS,
        OOH_THAT_IS_SO_FIRE;
    }

    public static class TestListValueFactory implements ListGroup.IValueFactory<String> {
        @Override
        public String provideNewValue() {
            return BuiltInRegistries.ITEM.getKey(item).toString();
        }
    }

    public static class TestListControllerFactory implements ListGroup.IControllerFactory<String> {

        @Override
        public IControllerBuilder<String> createController(ListGroup annotation, IConfigField<List<String>> field, IOptionAccess storage, IOption<String> option) {
            return IStringControllerBuilder.create(option);
        }
    }
}