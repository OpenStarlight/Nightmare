package cn.starlight.nightmare.client.item;

import cn.starlight.nightmare.config.NightmareConfig;
import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.player.NutritionSystem;
import cn.starlight.nightmare.util.render.StringUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class ItemTooltips {
    public static void initialize() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            NightmareConfig config = NightmareConfig.get();
            if (config.client.showNutritionTooltips) addNutritionTooltips(stack, lines);
            if (config.client.showGeneralTooltips) addGeneralTooltips(stack, lines);
        });
    }

    private static void addGeneralTooltips(ItemStack stack, List<Component> lines) {
        addTooltip(stack, lines, Items.WHEAT_SEEDS, "category.seeds");
        addTooltip(stack, lines, Items.PUMPKIN_SEEDS, "category.seeds");
        addTooltip(stack, lines, Items.MELON_SEEDS, "category.seeds");
        addTooltip(stack, lines, Items.BEETROOT_SEEDS, "category.seeds");
        addTooltip(stack, lines, Items.TORCHFLOWER_SEEDS, "category.seeds");
        addTooltip(stack, lines, Items.NETHER_WART, "category.seeds");
        addTooltip(stack, lines, ModItems.COPPER_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, ModItems.SILVER_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, ModItems.GOLD_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, ModItems.ANCIENT_METAL_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, ModItems.MITHRIL_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, ModItems.ADAMANTIUM_WATER_BUCKET, "category.buckets");
        addTooltip(stack, lines, Items.SUGAR, "minecraft.sugar");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_SWORD, "<green><lang:tooltip.category.silver_tool></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_AXE, "<green><lang:tooltip.category.silver_tool></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_PICKAXE, "<green><lang:tooltip.category.silver_tool></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_SHOVEL, "<green><lang:tooltip.category.silver_tool></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_HOE, "<green><lang:tooltip.category.silver_tool></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_HELMET, "<green><lang:tooltip.category.silver_armor></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_CHESTPLATE, "<green><lang:tooltip.category.silver_armor></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_LEGGINGS, "<green><lang:tooltip.category.silver_armor></green>");
        addTooltipCustomStyle(stack, lines, ModItems.SILVER_BOOTS, "<green><lang:tooltip.category.silver_armor></green>");
    }

    private static void addNutritionTooltips(ItemStack stack, List<Component> lines) {
        for (Map.Entry<Item, NutritionSystem.NutritionValue> entry : NutritionSystem.FOOD_NUTRITION_MAP.entrySet()) {
            if (!stack.is(entry.getKey())) continue;
            if ((int)entry.getValue().protein() > 0)
                lines.add(StringUtil.toComponent("<green><!italic><lang:tooltip.nightmare.nutrition_protein:'" + (int)entry.getValue().protein() + "'></!italic></green>"));
            if ((int)entry.getValue().phytonutrient() > 0)
                lines.add(StringUtil.toComponent("<green><!italic><lang:tooltip.nightmare.nutrition_phytonutrient:'" + (int)entry.getValue().phytonutrient() + "'></!italic></green>"));
            if ((int)entry.getValue().insulinResistance() > 0)
                lines.add(StringUtil.toComponent("<red><!italic><lang:tooltip.nightmare.nutrition_insulinResistance:'" + (int)entry.getValue().insulinResistance() + "'></!italic></red>"));
            return;
        }
    }

    private static void addTooltip(ItemStack stack, List<Component> lines, Item item, String itemKey) {
        if (stack.is(item)) lines.add(StringUtil.toComponent("<gray><italic>\"<lang:tooltip." + itemKey + ">\"</italic></gray>"));
    }

    private static void addTooltipCustomStyle(ItemStack stack, List<Component> lines, Item item, String string) {
        if (stack.is(item)) lines.add(StringUtil.toComponent(string));
    }
}
