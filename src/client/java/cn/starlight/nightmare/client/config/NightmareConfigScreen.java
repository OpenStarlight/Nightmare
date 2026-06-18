package cn.starlight.nightmare.client.config;

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
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.nightmare.client"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.nightmare.show_general_tooltips"), config.showGeneralTooltips)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.showGeneralTooltips = value)
                .build());
        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.nightmare.show_nutrition_tooltips"), config.showNutritionTooltips)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.showNutritionTooltips = value)
                .build());

        builder.setSavingRunnable(NightmareConfig::save);
        return builder.build();
    }
}
