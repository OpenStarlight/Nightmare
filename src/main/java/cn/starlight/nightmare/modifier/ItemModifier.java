package cn.starlight.nightmare.modifier;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.util.item.ItemUtil;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ItemModifier {
    private static final BlocksAttacks TOOL_BLOCKING = new BlocksAttacks(
            0.0F,
            0.0F,
            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 0.5F)),
            new BlocksAttacks.ItemDamageFunction(0.001F, 0.999F, 1.0F),
            Optional.empty(),
            Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.IRON_PLACE)),
            Optional.empty()
    );


    public static void initialize() {
        // 移除物品
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, creativeTab) -> {
            creativeTab.getDisplayStacks().removeIf(ItemModifier::isForbiddenItem);
            creativeTab.getSearchTabStacks().removeIf(ItemModifier::isForbiddenItem);
        });

        // 可以吃的
        Item[] eatable = new Item[]{
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.COCOA_BEANS,
                Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.NETHER_WART, Items.SUGAR
        };
        for (Item item : eatable) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            builder.set(DataComponents.CONSUMABLE, Consumable.builder().build());
        }));

        // 回复1饱和度的食物
        Item[] foodsOneSaturation = new Item[]{
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.COCOA_BEANS,
                Items.NETHER_WART, Items.SUGAR
        };
        for (Item item : foodsOneSaturation) ItemUtil.modifyFoodProperties(item, 0, 1f);

        // 回复1饱食度和1饱和度的食物
        Item[] foodsOneHunger = new Item[]{
                Items.SWEET_BERRIES, Items.MELON_SLICE, Items.GLOW_BERRIES, Items.MUTTON, Items.CHICKEN, Items.RABBIT, Items.COD, Items.SALMON,
                Items.TROPICAL_FISH, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM
        };
        for (Item item : foodsOneHunger) ItemUtil.modifyFoodProperties(item, 1, 1f);

        // 回复2饱食度和2饱和度的食物
        Item[] foodsTwoHunger = new Item[]{
                Items.APPLE, Items.CHORUS_FRUIT, Items.CARROT, Items.BEEF, Items.PORKCHOP, Items.ROTTEN_FLESH
        };
        for (Item item : foodsTwoHunger) ItemUtil.modifyFoodProperties(item, 2, 2f);

        // 其他食物
        ItemUtil.modifyFoodProperties(Items.BAKED_POTATO, 3, 4f);
        ItemUtil.modifyFoodProperties(Items.COOKED_BEEF, 6, 8f);
        ItemUtil.modifyFoodProperties(Items.COOKED_PORKCHOP, 6, 8f);
        ItemUtil.modifyFoodProperties(Items.COOKED_MUTTON, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.COOKED_RABBIT, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.COOKED_CHICKEN, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.COOKED_COD, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.COOKED_SALMON, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.BREAD, 4, 4f);
        ItemUtil.modifyFoodProperties(Items.COOKIE, 2, 2f);
        ItemUtil.modifyFoodProperties(Items.PUMPKIN_PIE, 6, 6f);
        ItemUtil.modifyFoodProperties(Items.MUSHROOM_STEW, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.SUSPICIOUS_STEW, 4, 6f);
        ItemUtil.modifyFoodProperties(Items.BEETROOT_SOUP, 6, 8f);

        // 碗类食品最大堆叠改为4
        Item[] bowlFoods = new Item[]{
                Items.MUSHROOM_STEW, Items.SUSPICIOUS_STEW, Items.RABBIT_STEW, Items.BEETROOT_SOUP
        };
        for (Item item : bowlFoods) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            builder.set(DataComponents.MAX_STACK_SIZE, 4);
        }));

        // 调整工具数值
        Map<Item, Double> toolDamage = Map.ofEntries(
                Map.entry(Items.GOLDEN_SWORD, 4.0),
                Map.entry(Items.COPPER_SWORD, 6.0),
                Map.entry(Items.IRON_SWORD, 7.0),
                Map.entry(Items.DIAMOND_SWORD, 9.0),
                Map.entry(Items.NETHERITE_SWORD, 11.0),
                Map.entry(Items.GOLDEN_AXE, 5.0),
                Map.entry(Items.COPPER_AXE, 7.0),
                Map.entry(Items.IRON_AXE, 8.0),
                Map.entry(Items.DIAMOND_AXE, 10.0),
                Map.entry(Items.NETHERITE_AXE, 12.0),
                Map.entry(Items.GOLDEN_PICKAXE, 3.0),
                Map.entry(Items.COPPER_PICKAXE, 5.0),
                Map.entry(Items.IRON_PICKAXE, 6.0),
                Map.entry(Items.DIAMOND_PICKAXE, 8.0),
                Map.entry(Items.NETHERITE_PICKAXE, 10.0),
                Map.entry(Items.GOLDEN_SHOVEL, 3.0),
                Map.entry(Items.COPPER_SHOVEL, 5.0),
                Map.entry(Items.IRON_SHOVEL, 6.0),
                Map.entry(Items.DIAMOND_SHOVEL, 8.0),
                Map.entry(Items.NETHERITE_SHOVEL, 10.0),
                Map.entry(Items.WOODEN_HOE, 1.0),
                Map.entry(Items.STONE_HOE, 1.0),
                Map.entry(Items.GOLDEN_HOE, 1.0),
                Map.entry(Items.COPPER_HOE, 1.0),
                Map.entry(Items.IRON_HOE, 1.0),
                Map.entry(Items.DIAMOND_HOE, 1.0),
                Map.entry(Items.NETHERITE_HOE, 1.0)
        );
        for (Map.Entry<Item, Double> entry : toolDamage.entrySet()) ItemUtil.modifyAttackDamage(entry.getKey(), entry.getValue());

        Map<Item, Double> toolSpeed = Map.ofEntries(
                Map.entry(Items.GOLDEN_AXE, 1.0),
                Map.entry(Items.COPPER_AXE, 1.0),
                Map.entry(Items.IRON_AXE, 1.0),
                Map.entry(Items.DIAMOND_AXE, 1.0),
                Map.entry(Items.NETHERITE_AXE, 1.0)
        );
        for (Map.Entry<Item, Double> entry : toolSpeed.entrySet()) ItemUtil.modifyAttackSpeed(entry.getKey(), entry.getValue());

        ItemUtil.modifyArmor(Items.LEATHER_HELMET, ArmorType.HELMET, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.LEATHER_CHESTPLATE, ArmorType.CHESTPLATE, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.LEATHER_LEGGINGS, ArmorType.LEGGINGS, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.LEATHER_BOOTS, ArmorType.BOOTS, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.CHAINMAIL_HELMET, ArmorType.HELMET, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.CHAINMAIL_CHESTPLATE, ArmorType.CHESTPLATE, 3, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.CHAINMAIL_LEGGINGS, ArmorType.LEGGINGS, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.CHAINMAIL_BOOTS, ArmorType.BOOTS, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.GOLDEN_HELMET, ArmorType.HELMET, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.GOLDEN_CHESTPLATE, ArmorType.CHESTPLATE, 3, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.GOLDEN_LEGGINGS, ArmorType.LEGGINGS, 3, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.GOLDEN_BOOTS, ArmorType.BOOTS, 1, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.COPPER_HELMET, ArmorType.HELMET, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.COPPER_CHESTPLATE, ArmorType.CHESTPLATE, 3, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.COPPER_LEGGINGS, ArmorType.LEGGINGS, 3, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.COPPER_BOOTS, ArmorType.BOOTS, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.IRON_HELMET, ArmorType.HELMET, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.IRON_CHESTPLATE, ArmorType.CHESTPLATE, 4, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.IRON_LEGGINGS, ArmorType.LEGGINGS, 4, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.IRON_BOOTS, ArmorType.BOOTS, 2, 0.0F, 0.0F);
        ItemUtil.modifyArmor(Items.DIAMOND_HELMET, ArmorType.HELMET, 3, 2.0F, 0.0F);
        ItemUtil.modifyArmor(Items.DIAMOND_CHESTPLATE, ArmorType.CHESTPLATE, 5, 2.0F, 0.0F);
        ItemUtil.modifyArmor(Items.DIAMOND_LEGGINGS, ArmorType.LEGGINGS, 5, 2.0F, 0.0F);
        ItemUtil.modifyArmor(Items.DIAMOND_BOOTS, ArmorType.BOOTS, 3, 2.0F, 0.0F);
        ItemUtil.modifyArmor(Items.NETHERITE_HELMET, ArmorType.HELMET, 3, 3.0F, 0.1F);
        ItemUtil.modifyArmor(Items.NETHERITE_CHESTPLATE, ArmorType.CHESTPLATE, 6, 3.0F, 0.1F);
        ItemUtil.modifyArmor(Items.NETHERITE_LEGGINGS, ArmorType.LEGGINGS, 6, 3.0F, 0.1F);
        ItemUtil.modifyArmor(Items.NETHERITE_BOOTS, ArmorType.BOOTS, 3, 3.0F, 0.1F);
        ItemUtil.modifyArmor(Items.TURTLE_HELMET, ArmorType.HELMET, 3, 0.0F, 0.0F);

        Map<Item, ResourceKey<TrimMaterial>> trimMaterials = Map.of(
                ModItems.SILVER_INGOT, ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "silver")),
                ModItems.ANCIENT_METAL_INGOT, ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "ancient_metal")),
                ModItems.MITHRIL_INGOT, ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "mithril")),
                ModItems.ADAMANTIUM_INGOT, ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "adamantium"))
        );
        for (Map.Entry<Item, ResourceKey<TrimMaterial>> entry : trimMaterials.entrySet()) {
            DefaultItemComponentEvents.MODIFY.register(context -> context.modify(entry.getKey(), (builder, lookupProvider, item) -> {
                builder.set(DataComponents.PROVIDES_TRIM_MATERIAL,
                        lookupProvider.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(entry.getValue()));
            }));
        }

        Item[] blockingTools = new Item[]{
                Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.COPPER_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
                Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.COPPER_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE,
                Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.COPPER_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE,
                Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.GOLDEN_SHOVEL, Items.COPPER_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL,
                Items.WOODEN_HOE, Items.STONE_HOE, Items.GOLDEN_HOE, Items.COPPER_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE,
                ModItems.FLINT_AXE, ModItems.FLINT_HATCHET, ModItems.FLINT_KNIFE, ModItems.FLINT_SHOVEL,
                ModItems.OBSIDIAN_AXE, ModItems.OBSIDIAN_SHOVEL,
                ModItems.SILVER_SWORD, ModItems.SILVER_PICKAXE, ModItems.SILVER_AXE, ModItems.SILVER_SHOVEL, ModItems.SILVER_HOE,
                ModItems.RUSTED_IRON_SWORD, ModItems.RUSTED_IRON_PICKAXE, ModItems.RUSTED_IRON_AXE, ModItems.RUSTED_IRON_SHOVEL, ModItems.RUSTED_IRON_HOE,
                ModItems.ANCIENT_METAL_SWORD, ModItems.ANCIENT_METAL_PICKAXE, ModItems.ANCIENT_METAL_AXE, ModItems.ANCIENT_METAL_SHOVEL, ModItems.ANCIENT_METAL_HOE,
                ModItems.MITHRIL_SWORD, ModItems.MITHRIL_PICKAXE, ModItems.MITHRIL_AXE, ModItems.MITHRIL_SHOVEL, ModItems.MITHRIL_HOE,
                ModItems.ADAMANTIUM_SWORD, ModItems.ADAMANTIUM_PICKAXE, ModItems.ADAMANTIUM_AXE, ModItems.ADAMANTIUM_SHOVEL, ModItems.ADAMANTIUM_HOE
        };
        for (Item item : blockingTools) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            builder.set(DataComponents.BLOCKS_ATTACKS, TOOL_BLOCKING);
        }));
    }

    public static boolean isBlockingTool(ItemStack stack) {
        return stack.is(Items.WOODEN_SWORD) || stack.is(Items.STONE_SWORD) || stack.is(Items.GOLDEN_SWORD) || stack.is(Items.COPPER_SWORD) || stack.is(Items.IRON_SWORD) || stack.is(Items.DIAMOND_SWORD) || stack.is(Items.NETHERITE_SWORD) ||
                stack.is(Items.WOODEN_PICKAXE) || stack.is(Items.STONE_PICKAXE) || stack.is(Items.GOLDEN_PICKAXE) || stack.is(Items.COPPER_PICKAXE) || stack.is(Items.IRON_PICKAXE) || stack.is(Items.DIAMOND_PICKAXE) || stack.is(Items.NETHERITE_PICKAXE) ||
                stack.is(Items.WOODEN_AXE) || stack.is(Items.STONE_AXE) || stack.is(Items.GOLDEN_AXE) || stack.is(Items.COPPER_AXE) || stack.is(Items.IRON_AXE) || stack.is(Items.DIAMOND_AXE) || stack.is(Items.NETHERITE_AXE) ||
                stack.is(Items.WOODEN_SHOVEL) || stack.is(Items.STONE_SHOVEL) || stack.is(Items.GOLDEN_SHOVEL) || stack.is(Items.COPPER_SHOVEL) || stack.is(Items.IRON_SHOVEL) || stack.is(Items.DIAMOND_SHOVEL) || stack.is(Items.NETHERITE_SHOVEL) ||
                stack.is(Items.WOODEN_HOE) || stack.is(Items.STONE_HOE) || stack.is(Items.GOLDEN_HOE) || stack.is(Items.COPPER_HOE) || stack.is(Items.IRON_HOE) || stack.is(Items.DIAMOND_HOE) || stack.is(Items.NETHERITE_HOE) ||
                stack.is(ModItems.FLINT_AXE) || stack.is(ModItems.FLINT_HATCHET) || stack.is(ModItems.FLINT_KNIFE) || stack.is(ModItems.FLINT_SHOVEL) ||
                stack.is(ModItems.OBSIDIAN_AXE) || stack.is(ModItems.OBSIDIAN_SHOVEL) ||
                stack.is(ModItems.SILVER_SWORD) || stack.is(ModItems.SILVER_PICKAXE) || stack.is(ModItems.SILVER_AXE) || stack.is(ModItems.SILVER_SHOVEL) || stack.is(ModItems.SILVER_HOE) ||
                stack.is(ModItems.RUSTED_IRON_SWORD) || stack.is(ModItems.RUSTED_IRON_PICKAXE) || stack.is(ModItems.RUSTED_IRON_AXE) || stack.is(ModItems.RUSTED_IRON_SHOVEL) || stack.is(ModItems.RUSTED_IRON_HOE) ||
                stack.is(ModItems.ANCIENT_METAL_SWORD) || stack.is(ModItems.ANCIENT_METAL_PICKAXE) || stack.is(ModItems.ANCIENT_METAL_AXE) || stack.is(ModItems.ANCIENT_METAL_SHOVEL) || stack.is(ModItems.ANCIENT_METAL_HOE) ||
                stack.is(ModItems.MITHRIL_SWORD) || stack.is(ModItems.MITHRIL_PICKAXE) || stack.is(ModItems.MITHRIL_AXE) || stack.is(ModItems.MITHRIL_SHOVEL) || stack.is(ModItems.MITHRIL_HOE) ||
                stack.is(ModItems.ADAMANTIUM_SWORD) || stack.is(ModItems.ADAMANTIUM_PICKAXE) || stack.is(ModItems.ADAMANTIUM_AXE) || stack.is(ModItems.ADAMANTIUM_SHOVEL) || stack.is(ModItems.ADAMANTIUM_HOE);
    }

    public static boolean isForbiddenItem(ItemStack stack) {
        return stack.is(Items.WOODEN_AXE) || stack.is(Items.WOODEN_PICKAXE) || stack.is(Items.WOODEN_SHOVEL) || stack.is(Items.WOODEN_HOE) || stack.is(Items.WOODEN_SWORD) ||
                stack.is(Items.WOODEN_SPEAR) || stack.is(Items.STONE_PICKAXE) || stack.is(Items.STONE_AXE) || stack.is(Items.STONE_HOE) || stack.is(Items.STONE_SHOVEL) ||
                stack.is(Items.STONE_SPEAR) || stack.is(Items.GOLDEN_SPEAR) || stack.is(Items.COPPER_SPEAR) || stack.is(Items.IRON_SPEAR) ||
                stack.is(Items.DIAMOND_SPEAR) || stack.is(Items.NETHERITE_SPEAR) || stack.is(Items.STONE_SWORD);
    }

    public static boolean isDrinkingItem(ItemStack stack) {
        return stack.is(ModItems.BOWL_WATER) || stack.is(ModItems.BOWL_MILK) || stack.is(ModItems.COPPER_MILK_BUCKET) || stack.is(ModItems.SILVER_MILK_BUCKET) ||
                stack.is(ModItems.GOLD_MILK_BUCKET) || stack.is(ModItems.ANCIENT_METAL_MILK_BUCKET) || stack.is(ModItems.MITHRIL_MILK_BUCKET) ||
                stack.is(ModItems.ADAMANTIUM_MILK_BUCKET);
    }
}
