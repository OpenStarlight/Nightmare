package cn.starlight.nightmare.item;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.item.impl.BowlWaterItem;
import cn.starlight.nightmare.item.impl.CerealItem;
import cn.starlight.nightmare.util.RegistryUtil;
import cn.starlight.nightmare.util.item.ItemUtil;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;

public class ModItems {
    // Shards and basic materials
    public static final Item FLINT_SHARD = RegistryUtil.registerItem("flint_shard", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_SHARD = RegistryUtil.registerItem("obsidian_shard", Item::new, new Item.Properties());
    public static final Item EMERALD_SHARD = RegistryUtil.registerItem("emerald_shard", Item::new, new Item.Properties());
    public static final Item DIAMOND_SHARD = RegistryUtil.registerItem("diamond_shard", Item::new, new Item.Properties());
    public static final Item QUARTZ_SHARD = RegistryUtil.registerItem("quartz_shard", Item::new, new Item.Properties());
    public static final Item GLASS_SHARD = RegistryUtil.registerItem("glass_shard", Item::new, new Item.Properties());
    public static final Item LEATHER_STRING = RegistryUtil.registerItem("leather_string", Item::new, new Item.Properties());

    // Raw materials
    public static final Item RAW_SILVER = RegistryUtil.registerItem("raw_silver", Item::new, new Item.Properties());
    public static final Item RAW_MITHRIL = RegistryUtil.registerItem("raw_mithril", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_SHARD = RegistryUtil.registerItem("adamantium_shard", Item::new, new Item.Properties());

    // Nuggets
    public static final Item SILVER_NUGGET = RegistryUtil.registerItem("silver_nugget", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_NUGGET = RegistryUtil.registerItem("ancient_metal_nugget", Item::new, new Item.Properties());
    public static final Item MITHRIL_NUGGET = RegistryUtil.registerItem("mithril_nugget", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_NUGGET = RegistryUtil.registerItem("adamantium_nugget", Item::new, new Item.Properties());

    // Ingots
    public static final Item SILVER_INGOT = RegistryUtil.registerItem("silver_ingot", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_INGOT = RegistryUtil.registerItem("ancient_metal_ingot", Item::new, new Item.Properties());
    public static final Item MITHRIL_INGOT = RegistryUtil.registerItem("mithril_ingot", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_INGOT = RegistryUtil.registerItem("adamantium_ingot", Item::new, new Item.Properties());

    // Foods
    public static final Item BLUE_BERRIES = ModBlocks.BLUE_BERRY_BUSH.asItem();
    public static final Item BOWL_WATER = RegistryUtil.registerItem("bowl_water", BowlWaterItem::new, new Item.Properties()
            .food(new FoodProperties(0, 0.0F, true), Consumables.DEFAULT_DRINK)
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item CEREAL = RegistryUtil.registerItem("cereal", CerealItem::new, new Item.Properties()
            .food(new FoodProperties(2, 4.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));
    public static final Item SALAD = RegistryUtil.registerItem("salad", Item::new, new Item.Properties()
            .food(new FoodProperties(1, 2.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));

    // Flint tools
    public static final Item FLINT_AXE = RegistryUtil.registerItem("flint_axe", Item::new, ItemUtil.removeComponent(new Item.Properties().axe(
            ModToolMaterials.FLINT, 3F, -3F), DataComponents.ENCHANTABLE));
    public static final Item FLINT_HATCHET = RegistryUtil.registerItem("flint_hatchet", Item::new, ItemUtil.removeComponent(new Item.Properties().axe(
            ModToolMaterials.FLINT, 2F, -3F).durability(5), DataComponents.ENCHANTABLE));
    public static final Item FLINT_KNIFE = RegistryUtil.registerItem("flint_knife", Item::new, ItemUtil.removeComponent(new Item.Properties().sword(
            ModToolMaterials.FLINT, 2F, -2.4F), DataComponents.ENCHANTABLE));
    public static final Item FLINT_SHOVEL = RegistryUtil.registerItem("flint_shovel", Item::new, ItemUtil.removeComponent(new Item.Properties().shovel(
            ModToolMaterials.FLINT, 1F, -3F), DataComponents.ENCHANTABLE));

    // Obsidian tools
    public static final Item OBSIDIAN_AXE = RegistryUtil.registerItem("obsidian_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.OBSIDIAN, 2F, -3F));
    public static final Item OBSIDIAN_SHOVEL = RegistryUtil.registerItem("obsidian_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.OBSIDIAN, 0F, -3F));

    // Silver tools
    public static final Item SILVER_AXE = RegistryUtil.registerItem("silver_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.SILVER, 2F, -3F));
    public static final Item SILVER_PICKAXE = RegistryUtil.registerItem("silver_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.SILVER, 0F, -2.8F));
    public static final Item SILVER_SHOVEL = RegistryUtil.registerItem("silver_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.SILVER, 0F, -3F));
    public static final Item SILVER_HOE = RegistryUtil.registerItem("silver_hoe", Item::new, new Item.Properties().hoe(
            ModToolMaterials.SILVER, -3F, -2.4F));
    public static final Item SILVER_SWORD = RegistryUtil.registerItem("silver_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.SILVER, 1F, -2.4F));

    // Rusted iron tools
    public static final Item RUSTED_IRON_AXE = RegistryUtil.registerItem("rusted_iron_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.RUSTED_IRON, 2F, -3F));
    public static final Item RUSTED_IRON_PICKAXE = RegistryUtil.registerItem("rusted_iron_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.RUSTED_IRON, 0F, -2.8F));
    public static final Item RUSTED_IRON_SHOVEL = RegistryUtil.registerItem("rusted_iron_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.RUSTED_IRON, 0F, -3F));
    public static final Item RUSTED_IRON_HOE = RegistryUtil.registerItem("rusted_iron_hoe", Item::new, new Item.Properties().hoe(
            ModToolMaterials.RUSTED_IRON, -3F, -2.4F));
    public static final Item RUSTED_IRON_SWORD = RegistryUtil.registerItem("rusted_iron_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.RUSTED_IRON, 1F, -2.4F));

    // Ancient metal tools
    public static final Item ANCIENT_METAL_AXE = RegistryUtil.registerItem("ancient_metal_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.ANCIENT_METAL, 2F, -3F));
    public static final Item ANCIENT_METAL_PICKAXE = RegistryUtil.registerItem("ancient_metal_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.ANCIENT_METAL, 0F, -2.8F));
    public static final Item ANCIENT_METAL_SHOVEL = RegistryUtil.registerItem("ancient_metal_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.ANCIENT_METAL, 0F, -3F));
    public static final Item ANCIENT_METAL_HOE = RegistryUtil.registerItem("ancient_metal_hoe", Item::new, new Item.Properties().hoe(
            ModToolMaterials.ANCIENT_METAL, -6F, -2.4F));
    public static final Item ANCIENT_METAL_SWORD = RegistryUtil.registerItem("ancient_metal_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.ANCIENT_METAL, 1F, -2.4F));

    // Mithril tools
    public static final Item MITHRIL_AXE = RegistryUtil.registerItem("mithril_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.MITHRIL, 2F, -3F));
    public static final Item MITHRIL_PICKAXE = RegistryUtil.registerItem("mithril_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.MITHRIL, 0F, -2.8F));
    public static final Item MITHRIL_SHOVEL = RegistryUtil.registerItem("mithril_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.MITHRIL, 0F, -3F));
    public static final Item MITHRIL_HOE = RegistryUtil.registerItem("mithril_hoe", Item::new, new Item.Properties().hoe(
            ModToolMaterials.MITHRIL, -8F, -2.4F));
    public static final Item MITHRIL_SWORD = RegistryUtil.registerItem("mithril_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.MITHRIL, 1F, -2.4F));

    // Adamantium tools
    public static final Item ADAMANTIUM_AXE = RegistryUtil.registerItem("adamantium_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.ADAMANTIUM, 2F, -3F));
    public static final Item ADAMANTIUM_PICKAXE = RegistryUtil.registerItem("adamantium_pickaxe", Item::new, new Item.Properties().pickaxe(
            ModToolMaterials.ADAMANTIUM, 0F, -2.8F));
    public static final Item ADAMANTIUM_SHOVEL = RegistryUtil.registerItem("adamantium_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.ADAMANTIUM, 0F, -3F));
    public static final Item ADAMANTIUM_HOE = RegistryUtil.registerItem("adamantium_hoe", Item::new, new Item.Properties().hoe(
            ModToolMaterials.ADAMANTIUM, -10F, -2.4F));
    public static final Item ADAMANTIUM_SWORD = RegistryUtil.registerItem("adamantium_sword", Item::new, new Item.Properties().sword(
            ModToolMaterials.ADAMANTIUM, 1F, -2.4F));

    public static void initialize() {
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
                    creativeTab.insertAfter(BOWL_WATER, CEREAL);
                    creativeTab.insertAfter(CEREAL, SALAD);
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
                });
    }
}
