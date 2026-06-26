package cn.starlight.nightmare.client.config;

import cn.starlight.nightmare.config.NightmareConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NightmareConfigScreen {
    public static Screen create(Screen parent) {
        NightmareConfig config = NightmareConfig.get();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.nightmare.config"));
        ConfigCategory clientCategory = builder.getOrCreateCategory(Component.translatable("category.nightmare.client"));
        ConfigCategory serverCategory = builder.getOrCreateCategory(Component.translatable("category.nightmare.server"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        clientCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.nightmare.show_general_tooltips"), config.client.showGeneralTooltips)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.client.showGeneralTooltips = value)
                .build());
        clientCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.nightmare.show_nutrition_tooltips"), config.client.showNutritionTooltips)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.client.showNutritionTooltips = value)
                .build());
        serverCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.nightmare.async_structure_search"), config.server.asyncStructureSearch)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.server.asyncStructureSearch = value)
                .build());

        builder.setSavingRunnable(NightmareConfig::save);
        return builder.build();
    }
}
