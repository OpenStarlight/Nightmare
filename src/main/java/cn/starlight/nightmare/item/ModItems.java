package cn.starlight.nightmare.item;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.item.impl.BowlMilkItem;
import cn.starlight.nightmare.item.impl.BowlWaterItem;
import cn.starlight.nightmare.util.item.BucketMelting;
import cn.starlight.nightmare.item.impl.CerealItem;
import cn.starlight.nightmare.item.impl.CopperBucketItem;
import cn.starlight.nightmare.item.impl.PowderSnowBucketItem;
import cn.starlight.nightmare.util.RegistryUtil;
import cn.starlight.nightmare.util.item.ItemUtil;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class ModItems {
    // 基本材料
    public static final Item LEATHER_STRING = RegistryUtil.registerItem("leather_string", Item::new, new Item.Properties());

    // 碎片
    public static final Item FLINT_SHARD = RegistryUtil.registerItem("flint_shard", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_SHARD = RegistryUtil.registerItem("obsidian_shard", Item::new, new Item.Properties());
    public static final Item EMERALD_SHARD = RegistryUtil.registerItem("emerald_shard", Item::new, new Item.Properties());
    public static final Item DIAMOND_SHARD = RegistryUtil.registerItem("diamond_shard", Item::new, new Item.Properties());
    public static final Item QUARTZ_SHARD = RegistryUtil.registerItem("quartz_shard", Item::new, new Item.Properties());
    public static final Item GLASS_SHARD = RegistryUtil.registerItem("glass_shard", Item::new, new Item.Properties());

    // 粗矿
    public static final Item RAW_SILVER = RegistryUtil.registerItem("raw_silver", Item::new, new Item.Properties());
    public static final Item RAW_MITHRIL = RegistryUtil.registerItem("raw_mithril", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_SHARD = RegistryUtil.registerItem("adamantium_shard", Item::new, new Item.Properties().fireResistant());

    // 矿粒
    public static final Item SILVER_NUGGET = RegistryUtil.registerItem("silver_nugget", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_NUGGET = RegistryUtil.registerItem("ancient_metal_nugget", Item::new, new Item.Properties());
    public static final Item MITHRIL_NUGGET = RegistryUtil.registerItem("mithril_nugget", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_NUGGET = RegistryUtil.registerItem("adamantium_nugget", Item::new, new Item.Properties().fireResistant());

    // 矿锭
    public static final Item SILVER_INGOT = RegistryUtil.registerItem("silver_ingot", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_INGOT = RegistryUtil.registerItem("ancient_metal_ingot", Item::new, new Item.Properties());
    public static final Item MITHRIL_INGOT = RegistryUtil.registerItem("mithril_ingot", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_INGOT = RegistryUtil.registerItem("adamantium_ingot", Item::new, new Item.Properties().fireResistant());

    // 容器
    public static final Item COPPER_BUCKET = RegistryUtil.registerItem("copper_bucket", properties ->
            new CopperBucketItem("copper", 0.16F, properties), bucketProperties(false));
    public static final Item COPPER_WATER_BUCKET = RegistryUtil.registerItem("copper_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, COPPER_BUCKET, properties), filledBucketProperties(COPPER_BUCKET, false));
    public static final Item COPPER_LAVA_BUCKET = RegistryUtil.registerItem("copper_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, COPPER_BUCKET, properties), filledBucketProperties(COPPER_BUCKET, false));
    public static final Item COPPER_MILK_BUCKET = RegistryUtil.registerItem("copper_milk_bucket", Item::new,
            milkBucketProperties(COPPER_BUCKET, false));
    public static final Item COPPER_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("copper_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, COPPER_BUCKET, properties), filledBucketProperties(COPPER_BUCKET, false));
    public static final Item GOLD_BUCKET = RegistryUtil.registerItem("gold_bucket", properties ->
            new CopperBucketItem("gold", 0.20F, properties), bucketProperties(false));
    public static final Item GOLD_WATER_BUCKET = RegistryUtil.registerItem("gold_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, GOLD_BUCKET, properties), filledBucketProperties(GOLD_BUCKET, false));
    public static final Item GOLD_LAVA_BUCKET = RegistryUtil.registerItem("gold_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, GOLD_BUCKET, properties), filledBucketProperties(GOLD_BUCKET, false));
    public static final Item GOLD_MILK_BUCKET = RegistryUtil.registerItem("gold_milk_bucket", Item::new,
            milkBucketProperties(GOLD_BUCKET, false));
    public static final Item GOLD_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("gold_powder_snow_bucket",
            properties -> new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, GOLD_BUCKET, properties), filledBucketProperties(GOLD_BUCKET, false));
    public static final Item SILVER_BUCKET = RegistryUtil.registerItem("silver_bucket", properties ->
            new CopperBucketItem("silver", 0.16F, properties), bucketProperties(false));
    public static final Item SILVER_WATER_BUCKET = RegistryUtil.registerItem("silver_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, SILVER_BUCKET, properties), filledBucketProperties(SILVER_BUCKET, false));
    public static final Item SILVER_LAVA_BUCKET = RegistryUtil.registerItem("silver_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, SILVER_BUCKET, properties), filledBucketProperties(SILVER_BUCKET, false));
    public static final Item SILVER_MILK_BUCKET = RegistryUtil.registerItem("silver_milk_bucket", Item::new,
            milkBucketProperties(SILVER_BUCKET, false));
    public static final Item SILVER_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("silver_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, SILVER_BUCKET, properties), filledBucketProperties(SILVER_BUCKET, false));
    public static final Item ANCIENT_METAL_BUCKET = RegistryUtil.registerItem("ancient_metal_bucket", properties ->
            new CopperBucketItem("ancient_metal", 0.04F, properties), bucketProperties(false));
    public static final Item ANCIENT_METAL_WATER_BUCKET = RegistryUtil.registerItem("ancient_metal_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, ANCIENT_METAL_BUCKET, properties), filledBucketProperties(ANCIENT_METAL_BUCKET, false));
    public static final Item ANCIENT_METAL_LAVA_BUCKET = RegistryUtil.registerItem("ancient_metal_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, ANCIENT_METAL_BUCKET, properties), filledBucketProperties(ANCIENT_METAL_BUCKET, false));
    public static final Item ANCIENT_METAL_MILK_BUCKET = RegistryUtil.registerItem("ancient_metal_milk_bucket", Item::new,
            milkBucketProperties(ANCIENT_METAL_BUCKET, false));
    public static final Item ANCIENT_METAL_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("ancient_metal_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, ANCIENT_METAL_BUCKET, properties), filledBucketProperties(ANCIENT_METAL_BUCKET, false));
    public static final Item MITHRIL_BUCKET = RegistryUtil.registerItem("mithril_bucket", properties ->
            new CopperBucketItem("mithril", 0.01F, properties), bucketProperties(false));
    public static final Item MITHRIL_WATER_BUCKET = RegistryUtil.registerItem("mithril_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, MITHRIL_BUCKET, properties), filledBucketProperties(MITHRIL_BUCKET, false));
    public static final Item MITHRIL_LAVA_BUCKET = RegistryUtil.registerItem("mithril_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, MITHRIL_BUCKET, properties), filledBucketProperties(MITHRIL_BUCKET, false));
    public static final Item MITHRIL_MILK_BUCKET = RegistryUtil.registerItem("mithril_milk_bucket", Item::new,
            milkBucketProperties(MITHRIL_BUCKET, false));
    public static final Item MITHRIL_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("mithril_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, MITHRIL_BUCKET, properties), filledBucketProperties(MITHRIL_BUCKET, false));
    public static final Item ADAMANTIUM_BUCKET = RegistryUtil.registerItem("adamantium_bucket", properties ->
            new CopperBucketItem("adamantium", 0.0F, properties), bucketProperties(true));
    public static final Item ADAMANTIUM_WATER_BUCKET = RegistryUtil.registerItem("adamantium_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, ADAMANTIUM_BUCKET, properties), filledBucketProperties(ADAMANTIUM_BUCKET, true));
    public static final Item ADAMANTIUM_LAVA_BUCKET = RegistryUtil.registerItem("adamantium_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, ADAMANTIUM_BUCKET, properties), filledBucketProperties(ADAMANTIUM_BUCKET, true));
    public static final Item ADAMANTIUM_MILK_BUCKET = RegistryUtil.registerItem("adamantium_milk_bucket", Item::new,
            milkBucketProperties(ADAMANTIUM_BUCKET, true));
    public static final Item ADAMANTIUM_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("adamantium_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, ADAMANTIUM_BUCKET, properties), filledBucketProperties(ADAMANTIUM_BUCKET, true));
    public static final Item DIAMOND_BUCKET = RegistryUtil.registerItem("diamond_bucket", properties ->
            new CopperBucketItem("diamond", 0.02F, properties), bucketProperties(false));
    public static final Item DIAMOND_WATER_BUCKET = RegistryUtil.registerItem("diamond_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, DIAMOND_BUCKET, properties), filledBucketProperties(DIAMOND_BUCKET, false));
    public static final Item DIAMOND_LAVA_BUCKET = RegistryUtil.registerItem("diamond_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, DIAMOND_BUCKET, properties), filledBucketProperties(DIAMOND_BUCKET, false));
    public static final Item DIAMOND_MILK_BUCKET = RegistryUtil.registerItem("diamond_milk_bucket", Item::new,
            milkBucketProperties(DIAMOND_BUCKET, false));
    public static final Item DIAMOND_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("diamond_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, DIAMOND_BUCKET, properties), filledBucketProperties(DIAMOND_BUCKET, false));
    public static final Item NETHERITE_BUCKET = RegistryUtil.registerItem("netherite_bucket", properties ->
            new CopperBucketItem("netherite", 0.0F, properties), bucketProperties(true));
    public static final Item NETHERITE_WATER_BUCKET = RegistryUtil.registerItem("netherite_water_bucket", properties ->
            new CopperBucketItem(Fluids.WATER, NETHERITE_BUCKET, properties), filledBucketProperties(NETHERITE_BUCKET, true));
    public static final Item NETHERITE_LAVA_BUCKET = RegistryUtil.registerItem("netherite_lava_bucket", properties ->
            new CopperBucketItem(Fluids.LAVA, NETHERITE_BUCKET, properties), filledBucketProperties(NETHERITE_BUCKET, true));
    public static final Item NETHERITE_MILK_BUCKET = RegistryUtil.registerItem("netherite_milk_bucket", Item::new,
            milkBucketProperties(NETHERITE_BUCKET, true));
    public static final Item NETHERITE_POWDER_SNOW_BUCKET = RegistryUtil.registerItem("netherite_powder_snow_bucket", properties ->
            new PowderSnowBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, NETHERITE_BUCKET, properties), filledBucketProperties(NETHERITE_BUCKET, true));

    // 食物
    public static final Item BLUE_BERRIES = ModBlocks.BLUE_BERRY_BUSH.asItem();
    public static final Item BOWL_WATER = RegistryUtil.registerItem("bowl_water", BowlWaterItem::new, new Item.Properties()
            .food(new FoodProperties(0, 0.0F, true), Consumables.DEFAULT_DRINK)
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item BOWL_MILK = RegistryUtil.registerItem("bowl_milk", BowlMilkItem::new, new Item.Properties()
            .food(new FoodProperties(0, 0.0F, true), Consumables.MILK_BUCKET)
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item CEREAL = RegistryUtil.registerItem("cereal", CerealItem::new, new Item.Properties()
            .food(new FoodProperties(2, 4.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item PORRIDGE = RegistryUtil.registerItem("porridge", CerealItem::new, new Item.Properties()
            .food(new FoodProperties(2, 4.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item SALAD = RegistryUtil.registerItem("salad", Item::new, new Item.Properties()
            .food(new FoodProperties(1, 2.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));

    // 燧石工具
    public static final Item FLINT_AXE = RegistryUtil.registerItem("flint_axe", Item::new, ItemUtil.removeComponent(new Item.Properties().axe(
            ModToolMaterials.FLINT, 3F, -3F), DataComponents.ENCHANTABLE));
    public static final Item FLINT_HATCHET = RegistryUtil.registerItem("flint_hatchet", Item::new, ItemUtil.removeComponent(new Item.Properties().axe(
            ModToolMaterials.FLINT, 2F, -3F).durability(5), DataComponents.ENCHANTABLE));
    public static final Item FLINT_KNIFE = RegistryUtil.registerItem("flint_knife", Item::new, ItemUtil.removeComponent(new Item.Properties().sword(
            ModToolMaterials.FLINT, 2F, -2.4F), DataComponents.ENCHANTABLE));
    public static final Item FLINT_SHOVEL = RegistryUtil.registerItem("flint_shovel", Item::new, ItemUtil.removeComponent(new Item.Properties().shovel(
            ModToolMaterials.FLINT, 1F, -3F), DataComponents.ENCHANTABLE));

    // 黑曜石工具
    public static final Item OBSIDIAN_AXE = RegistryUtil.registerItem("obsidian_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.OBSIDIAN, 2F, -3F));
    public static final Item OBSIDIAN_SHOVEL = RegistryUtil.registerItem("obsidian_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.OBSIDIAN, 0F, -3F));

    // 银工具
    public static final Item SILVER_AXE = RegistryUtil.registerItem("silver_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.SILVER, 2F, -3F));
    public static final Item SILVER_PICKAXE = RegistryUtil.registerItem("silver_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.SILVER, 0F, -2.8F));
    public static final Item SILVER_SHOVEL = RegistryUtil.registerItem("silver_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.SILVER, 0F, -3F));
    public static final Item SILVER_HOE = RegistryUtil.registerItem("silver_hoe", properties -> new HoeItem(
            ModToolMaterials.SILVER, -3F, -2.4F, properties), new Item.Properties());
    public static final Item SILVER_SWORD = RegistryUtil.registerItem("silver_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.SILVER, 1F, -2.4F));

    // 锈铁工具
    public static final Item RUSTED_IRON_AXE = RegistryUtil.registerItem("rusted_iron_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.RUSTED_IRON, 2F, -3F));
    public static final Item RUSTED_IRON_PICKAXE = RegistryUtil.registerItem("rusted_iron_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.RUSTED_IRON, 0F, -2.8F));
    public static final Item RUSTED_IRON_SHOVEL = RegistryUtil.registerItem("rusted_iron_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.RUSTED_IRON, 0F, -3F));
    public static final Item RUSTED_IRON_HOE = RegistryUtil.registerItem("rusted_iron_hoe", properties -> new HoeItem(
            ModToolMaterials.RUSTED_IRON, -3F, -2.4F, properties), new Item.Properties());
    public static final Item RUSTED_IRON_SWORD = RegistryUtil.registerItem("rusted_iron_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.RUSTED_IRON, 1F, -2.4F));

    // 远古金属工具
    public static final Item ANCIENT_METAL_AXE = RegistryUtil.registerItem("ancient_metal_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.ANCIENT_METAL, 2F, -3F));
    public static final Item ANCIENT_METAL_PICKAXE = RegistryUtil.registerItem("ancient_metal_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.ANCIENT_METAL, 0F, -2.8F));
    public static final Item ANCIENT_METAL_SHOVEL = RegistryUtil.registerItem("ancient_metal_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.ANCIENT_METAL, 0F, -3F));
    public static final Item ANCIENT_METAL_HOE = RegistryUtil.registerItem("ancient_metal_hoe", properties -> new HoeItem(
            ModToolMaterials.ANCIENT_METAL, -6F, -2.4F, properties), new Item.Properties());
    public static final Item ANCIENT_METAL_SWORD = RegistryUtil.registerItem("ancient_metal_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.ANCIENT_METAL, 1F, -2.4F));

    // 秘银工具
    public static final Item MITHRIL_AXE = RegistryUtil.registerItem("mithril_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.MITHRIL, 2F, -3F));
    public static final Item MITHRIL_PICKAXE = RegistryUtil.registerItem("mithril_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.MITHRIL, 0F, -2.8F));
    public static final Item MITHRIL_SHOVEL = RegistryUtil.registerItem("mithril_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.MITHRIL, 0F, -3F));
    public static final Item MITHRIL_HOE = RegistryUtil.registerItem("mithril_hoe", properties -> new HoeItem(
            ModToolMaterials.MITHRIL, -8F, -2.4F, properties), new Item.Properties());
    public static final Item MITHRIL_SWORD = RegistryUtil.registerItem("mithril_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.MITHRIL, 1F, -2.4F));

    // 艾德曼工具
    public static final Item ADAMANTIUM_AXE = RegistryUtil.registerItem("adamantium_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.ADAMANTIUM, 2F, -3F).fireResistant());
    public static final Item ADAMANTIUM_PICKAXE = RegistryUtil.registerItem("adamantium_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.ADAMANTIUM, 0F, -2.8F).fireResistant());
    public static final Item ADAMANTIUM_SHOVEL = RegistryUtil.registerItem("adamantium_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.ADAMANTIUM, 0F, -3F).fireResistant());
    public static final Item ADAMANTIUM_HOE = RegistryUtil.registerItem("adamantium_hoe", properties -> new HoeItem(
            ModToolMaterials.ADAMANTIUM, -10F, -2.4F, properties), new Item.Properties().fireResistant());
    public static final Item ADAMANTIUM_SWORD = RegistryUtil.registerItem("adamantium_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.ADAMANTIUM, 1F, -2.4F).fireResistant());

    // æŠ¤ç”²
    public static final Item RUSTED_IRON_HELMET = RegistryUtil.registerItem("rusted_iron_helmet", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.RUSTED_IRON, ArmorType.HELMET));
    public static final Item RUSTED_IRON_CHESTPLATE = RegistryUtil.registerItem("rusted_iron_chestplate", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.RUSTED_IRON, ArmorType.CHESTPLATE));
    public static final Item RUSTED_IRON_LEGGINGS = RegistryUtil.registerItem("rusted_iron_leggings", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.RUSTED_IRON, ArmorType.LEGGINGS));
    public static final Item RUSTED_IRON_BOOTS = RegistryUtil.registerItem("rusted_iron_boots", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.RUSTED_IRON, ArmorType.BOOTS));
    public static final Item SILVER_HELMET = RegistryUtil.registerItem("silver_helmet", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.SILVER, ArmorType.HELMET));
    public static final Item SILVER_CHESTPLATE = RegistryUtil.registerItem("silver_chestplate", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.SILVER, ArmorType.CHESTPLATE));
    public static final Item SILVER_LEGGINGS = RegistryUtil.registerItem("silver_leggings", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.SILVER, ArmorType.LEGGINGS));
    public static final Item SILVER_BOOTS = RegistryUtil.registerItem("silver_boots", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.SILVER, ArmorType.BOOTS));
    public static final Item ANCIENT_METAL_HELMET = RegistryUtil.registerItem("ancient_metal_helmet", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ANCIENT_METAL, ArmorType.HELMET));
    public static final Item ANCIENT_METAL_CHESTPLATE = RegistryUtil.registerItem("ancient_metal_chestplate", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ANCIENT_METAL, ArmorType.CHESTPLATE));
    public static final Item ANCIENT_METAL_LEGGINGS = RegistryUtil.registerItem("ancient_metal_leggings", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ANCIENT_METAL, ArmorType.LEGGINGS));
    public static final Item ANCIENT_METAL_BOOTS = RegistryUtil.registerItem("ancient_metal_boots", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ANCIENT_METAL, ArmorType.BOOTS));
    public static final Item MITHRIL_HELMET = RegistryUtil.registerItem("mithril_helmet", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.MITHRIL, ArmorType.HELMET));
    public static final Item MITHRIL_CHESTPLATE = RegistryUtil.registerItem("mithril_chestplate", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.MITHRIL, ArmorType.CHESTPLATE));
    public static final Item MITHRIL_LEGGINGS = RegistryUtil.registerItem("mithril_leggings", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.MITHRIL, ArmorType.LEGGINGS));
    public static final Item MITHRIL_BOOTS = RegistryUtil.registerItem("mithril_boots", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.MITHRIL, ArmorType.BOOTS));
    public static final Item ADAMANTIUM_HELMET = RegistryUtil.registerItem("adamantium_helmet", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ADAMANTIUM, ArmorType.HELMET).fireResistant());
    public static final Item ADAMANTIUM_CHESTPLATE = RegistryUtil.registerItem("adamantium_chestplate", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ADAMANTIUM, ArmorType.CHESTPLATE).fireResistant());
    public static final Item ADAMANTIUM_LEGGINGS = RegistryUtil.registerItem("adamantium_leggings", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ADAMANTIUM, ArmorType.LEGGINGS).fireResistant());
    public static final Item ADAMANTIUM_BOOTS = RegistryUtil.registerItem("adamantium_boots", Item::new, new Item.Properties().humanoidArmor(ModArmorMaterials.ADAMANTIUM, ArmorType.BOOTS).fireResistant());

    public static void initialize() {
        ModBucketBehaviors.registerCauldronInteractions();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> {
                    creativeTab.insertBefore(Items.FLINT, FLINT_SHARD);
                    creativeTab.insertAfter(FLINT_SHARD, OBSIDIAN_SHARD);
                    creativeTab.insertAfter(OBSIDIAN_SHARD, EMERALD_SHARD);
                    creativeTab.insertAfter(EMERALD_SHARD, DIAMOND_SHARD);
                    creativeTab.insertAfter(DIAMOND_SHARD, QUARTZ_SHARD);
                    creativeTab.insertAfter(QUARTZ_SHARD, GLASS_SHARD);
                    creativeTab.insertAfter(Items.STRING, LEATHER_STRING);
                    creativeTab.insertAfter(Items.GOLD_NUGGET, SILVER_NUGGET);
                    creativeTab.insertAfter(SILVER_NUGGET, ANCIENT_METAL_NUGGET);
                    creativeTab.insertAfter(ANCIENT_METAL_NUGGET, MITHRIL_NUGGET);
                    creativeTab.insertAfter(MITHRIL_NUGGET, ADAMANTIUM_NUGGET);
                    creativeTab.insertAfter(ADAMANTIUM_NUGGET, RAW_SILVER);
                    creativeTab.insertAfter(RAW_SILVER, RAW_MITHRIL);
                    creativeTab.insertAfter(RAW_MITHRIL, ADAMANTIUM_SHARD);
                    creativeTab.insertAfter(Items.IRON_INGOT, SILVER_INGOT);
                    creativeTab.insertAfter(SILVER_INGOT, ANCIENT_METAL_INGOT);
                    creativeTab.insertAfter(ANCIENT_METAL_INGOT, MITHRIL_INGOT);
                    creativeTab.insertAfter(MITHRIL_INGOT, ADAMANTIUM_INGOT);
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> {
                    creativeTab.insertAfter(Items.POWDER_SNOW_BUCKET, COPPER_BUCKET);
                    creativeTab.insertAfter(COPPER_BUCKET, COPPER_WATER_BUCKET);
                    creativeTab.insertAfter(COPPER_WATER_BUCKET, COPPER_LAVA_BUCKET);
                    creativeTab.insertAfter(COPPER_LAVA_BUCKET, COPPER_MILK_BUCKET);
                    creativeTab.insertAfter(COPPER_MILK_BUCKET, COPPER_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(COPPER_POWDER_SNOW_BUCKET, GOLD_BUCKET);
                    creativeTab.insertAfter(GOLD_BUCKET, GOLD_WATER_BUCKET);
                    creativeTab.insertAfter(GOLD_WATER_BUCKET, GOLD_LAVA_BUCKET);
                    creativeTab.insertAfter(GOLD_LAVA_BUCKET, GOLD_MILK_BUCKET);
                    creativeTab.insertAfter(GOLD_MILK_BUCKET, GOLD_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(GOLD_POWDER_SNOW_BUCKET, SILVER_BUCKET);
                    creativeTab.insertAfter(SILVER_BUCKET, SILVER_WATER_BUCKET);
                    creativeTab.insertAfter(SILVER_WATER_BUCKET, SILVER_LAVA_BUCKET);
                    creativeTab.insertAfter(SILVER_LAVA_BUCKET, SILVER_MILK_BUCKET);
                    creativeTab.insertAfter(SILVER_MILK_BUCKET, SILVER_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(SILVER_POWDER_SNOW_BUCKET, ANCIENT_METAL_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_BUCKET, ANCIENT_METAL_WATER_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_WATER_BUCKET, ANCIENT_METAL_LAVA_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_LAVA_BUCKET, ANCIENT_METAL_MILK_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_MILK_BUCKET, ANCIENT_METAL_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_POWDER_SNOW_BUCKET, MITHRIL_BUCKET);
                    creativeTab.insertAfter(MITHRIL_BUCKET, MITHRIL_WATER_BUCKET);
                    creativeTab.insertAfter(MITHRIL_WATER_BUCKET, MITHRIL_LAVA_BUCKET);
                    creativeTab.insertAfter(MITHRIL_LAVA_BUCKET, MITHRIL_MILK_BUCKET);
                    creativeTab.insertAfter(MITHRIL_MILK_BUCKET, MITHRIL_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(MITHRIL_POWDER_SNOW_BUCKET, ADAMANTIUM_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_BUCKET, ADAMANTIUM_WATER_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_WATER_BUCKET, ADAMANTIUM_LAVA_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_LAVA_BUCKET, ADAMANTIUM_MILK_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_MILK_BUCKET, ADAMANTIUM_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_POWDER_SNOW_BUCKET, DIAMOND_BUCKET);
                    creativeTab.insertAfter(DIAMOND_BUCKET, DIAMOND_WATER_BUCKET);
                    creativeTab.insertAfter(DIAMOND_WATER_BUCKET, DIAMOND_LAVA_BUCKET);
                    creativeTab.insertAfter(DIAMOND_LAVA_BUCKET, DIAMOND_MILK_BUCKET);
                    creativeTab.insertAfter(DIAMOND_MILK_BUCKET, DIAMOND_POWDER_SNOW_BUCKET);
                    creativeTab.insertAfter(DIAMOND_POWDER_SNOW_BUCKET, NETHERITE_BUCKET);
                    creativeTab.insertAfter(NETHERITE_BUCKET, NETHERITE_WATER_BUCKET);
                    creativeTab.insertAfter(NETHERITE_WATER_BUCKET, NETHERITE_LAVA_BUCKET);
                    creativeTab.insertAfter(NETHERITE_LAVA_BUCKET, NETHERITE_MILK_BUCKET);
                    creativeTab.insertAfter(NETHERITE_MILK_BUCKET, NETHERITE_POWDER_SNOW_BUCKET);
                    creativeTab.prepend(FLINT_SHOVEL);
                    creativeTab.insertAfter(FLINT_SHOVEL, FLINT_AXE);
                    creativeTab.insertAfter(FLINT_AXE, FLINT_HATCHET);
                    creativeTab.insertAfter(Items.GOLDEN_HOE, OBSIDIAN_SHOVEL);
                    creativeTab.insertAfter(OBSIDIAN_SHOVEL, OBSIDIAN_AXE);
                    creativeTab.insertAfter(OBSIDIAN_AXE, SILVER_SHOVEL);
                    creativeTab.insertAfter(SILVER_SHOVEL, SILVER_PICKAXE);
                    creativeTab.insertAfter(SILVER_PICKAXE, SILVER_AXE);
                    creativeTab.insertAfter(SILVER_AXE, SILVER_HOE);
                    creativeTab.insertAfter(SILVER_HOE, RUSTED_IRON_SHOVEL);
                    creativeTab.insertAfter(RUSTED_IRON_SHOVEL, RUSTED_IRON_PICKAXE);
                    creativeTab.insertAfter(RUSTED_IRON_PICKAXE, RUSTED_IRON_AXE);
                    creativeTab.insertAfter(RUSTED_IRON_AXE, RUSTED_IRON_HOE);
                    creativeTab.insertAfter(Items.IRON_HOE, ANCIENT_METAL_SHOVEL);
                    creativeTab.insertAfter(ANCIENT_METAL_SHOVEL, ANCIENT_METAL_PICKAXE);
                    creativeTab.insertAfter(ANCIENT_METAL_PICKAXE, ANCIENT_METAL_AXE);
                    creativeTab.insertAfter(ANCIENT_METAL_AXE, ANCIENT_METAL_HOE);
                    creativeTab.insertAfter(Items.DIAMOND_HOE, MITHRIL_SHOVEL);
                    creativeTab.insertAfter(MITHRIL_SHOVEL, MITHRIL_PICKAXE);
                    creativeTab.insertAfter(MITHRIL_PICKAXE, MITHRIL_AXE);
                    creativeTab.insertAfter(MITHRIL_AXE, MITHRIL_HOE);
                    creativeTab.insertAfter(Items.NETHERITE_HOE, ADAMANTIUM_SHOVEL);
                    creativeTab.insertAfter(ADAMANTIUM_SHOVEL, ADAMANTIUM_PICKAXE);
                    creativeTab.insertAfter(ADAMANTIUM_PICKAXE, ADAMANTIUM_AXE);
                    creativeTab.insertAfter(ADAMANTIUM_AXE, ADAMANTIUM_HOE);
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((creativeTab) -> {
                    creativeTab.insertBefore(Items.MUSHROOM_STEW, BOWL_WATER);
                    creativeTab.insertAfter(BOWL_WATER, BOWL_MILK);
                    creativeTab.insertAfter(Items.MILK_BUCKET, COPPER_MILK_BUCKET);
                    creativeTab.insertAfter(COPPER_MILK_BUCKET, GOLD_MILK_BUCKET);
                    creativeTab.insertAfter(GOLD_MILK_BUCKET, SILVER_MILK_BUCKET);
                    creativeTab.insertAfter(SILVER_MILK_BUCKET, ANCIENT_METAL_MILK_BUCKET);
                    creativeTab.insertAfter(ANCIENT_METAL_MILK_BUCKET, MITHRIL_MILK_BUCKET);
                    creativeTab.insertAfter(MITHRIL_MILK_BUCKET, ADAMANTIUM_MILK_BUCKET);
                    creativeTab.insertAfter(ADAMANTIUM_MILK_BUCKET, DIAMOND_MILK_BUCKET);
                    creativeTab.insertAfter(DIAMOND_MILK_BUCKET, NETHERITE_MILK_BUCKET);
                    creativeTab.insertAfter(BOWL_MILK, CEREAL);
                    creativeTab.insertAfter(CEREAL, PORRIDGE);
                    creativeTab.insertAfter(PORRIDGE, SALAD);
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> {
                    creativeTab.insertBefore(Items.COPPER_SWORD, FLINT_KNIFE);
                    creativeTab.insertBefore(Items.COPPER_AXE, FLINT_AXE);
                    creativeTab.insertAfter(Items.GOLDEN_SWORD, SILVER_SWORD);
                    creativeTab.insertAfter(SILVER_SWORD, RUSTED_IRON_SWORD);
                    creativeTab.insertAfter(Items.GOLDEN_AXE, OBSIDIAN_AXE);
                    creativeTab.insertAfter(OBSIDIAN_AXE, SILVER_AXE);
                    creativeTab.insertAfter(SILVER_AXE, RUSTED_IRON_AXE);
                    creativeTab.insertAfter(Items.IRON_SWORD, ANCIENT_METAL_SWORD);
                    creativeTab.insertAfter(Items.IRON_AXE, ANCIENT_METAL_AXE);
                    creativeTab.insertAfter(Items.DIAMOND_SWORD, MITHRIL_SWORD);
                    creativeTab.insertAfter(Items.DIAMOND_AXE, MITHRIL_AXE);
                    creativeTab.insertAfter(Items.NETHERITE_SWORD, ADAMANTIUM_SWORD);
                    creativeTab.insertAfter(Items.NETHERITE_AXE, ADAMANTIUM_AXE);
                    creativeTab.insertAfter(Items.GOLDEN_BOOTS, RUSTED_IRON_HELMET);
                    creativeTab.insertAfter(RUSTED_IRON_HELMET, RUSTED_IRON_CHESTPLATE);
                    creativeTab.insertAfter(RUSTED_IRON_CHESTPLATE, RUSTED_IRON_LEGGINGS);
                    creativeTab.insertAfter(RUSTED_IRON_LEGGINGS, RUSTED_IRON_BOOTS);
                    creativeTab.insertAfter(RUSTED_IRON_BOOTS, SILVER_HELMET);
                    creativeTab.insertAfter(SILVER_HELMET, SILVER_CHESTPLATE);
                    creativeTab.insertAfter(SILVER_CHESTPLATE, SILVER_LEGGINGS);
                    creativeTab.insertAfter(SILVER_LEGGINGS, SILVER_BOOTS);
                    creativeTab.insertAfter(Items.IRON_BOOTS, ANCIENT_METAL_HELMET);
                    creativeTab.insertAfter(ANCIENT_METAL_HELMET, ANCIENT_METAL_CHESTPLATE);
                    creativeTab.insertAfter(ANCIENT_METAL_CHESTPLATE, ANCIENT_METAL_LEGGINGS);
                    creativeTab.insertAfter(ANCIENT_METAL_LEGGINGS, ANCIENT_METAL_BOOTS);
                    creativeTab.insertAfter(Items.DIAMOND_BOOTS, MITHRIL_HELMET);
                    creativeTab.insertAfter(MITHRIL_HELMET, MITHRIL_CHESTPLATE);
                    creativeTab.insertAfter(MITHRIL_CHESTPLATE, MITHRIL_LEGGINGS);
                    creativeTab.insertAfter(MITHRIL_LEGGINGS, MITHRIL_BOOTS);
                    creativeTab.insertAfter(Items.NETHERITE_BOOTS, ADAMANTIUM_HELMET);
                    creativeTab.insertAfter(ADAMANTIUM_HELMET, ADAMANTIUM_CHESTPLATE);
                    creativeTab.insertAfter(ADAMANTIUM_CHESTPLATE, ADAMANTIUM_LEGGINGS);
                    creativeTab.insertAfter(ADAMANTIUM_LEGGINGS, ADAMANTIUM_BOOTS);
                });

        // Iron bucket melting
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.BUCKET)) return InteractionResult.PASS;

            net.minecraft.world.phys.BlockHitResult hitResult = CopperBucketItem.getSourceHitResult(level, player);
            if (hitResult.getType() != net.minecraft.world.phys.HitResult.Type.BLOCK) return InteractionResult.PASS;
            BlockPos pos = hitResult.getBlockPos();
            if (!level.getFluidState(pos).isSourceOfType(Fluids.LAVA)) return InteractionResult.PASS;

            return BucketMelting.tryMelt(level, player, hand, pos, 0.08F) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        });
    }

    private static Item.Properties bucketProperties(boolean fireResistant) {
        return maybeFireResistant(new Item.Properties().stacksTo(16), fireResistant);
    }

    private static Item.Properties filledBucketProperties(Item emptyBucket, boolean fireResistant) {
        return maybeFireResistant(new Item.Properties().stacksTo(1).craftRemainder(emptyBucket), fireResistant);
    }

    private static Item.Properties milkBucketProperties(Item emptyBucket, boolean fireResistant) {
        return maybeFireResistant(new Item.Properties()
                .food(new FoodProperties(0, 0.0F, true), Consumables.MILK_BUCKET)
                .usingConvertsTo(emptyBucket)
                .craftRemainder(emptyBucket)
                .stacksTo(1), fireResistant);
    }

    private static Item.Properties maybeFireResistant(Item.Properties properties, boolean fireResistant) {
        return fireResistant ? properties.fireResistant() : properties;
    }
}
