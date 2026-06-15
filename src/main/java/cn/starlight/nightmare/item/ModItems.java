package cn.starlight.nightmare.item;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.modifier.ItemModifier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ModItems {
    public static final Item FLINT_SHARD = register("flint_shard", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_SHARD = register("obsidian_shard", Item::new, new Item.Properties());
    public static final Item EMERALD_SHARD = register("emerald_shard", Item::new, new Item.Properties());
    public static final Item DIAMOND_SHARD = register("diamond_shard", Item::new, new Item.Properties());
    public static final Item QUARTZ_SHARD = register("quartz_shard", Item::new, new Item.Properties());
    public static final Item GLASS_SHARD = register("glass_shard", Item::new, new Item.Properties());
    public static final Item LEATHER_STRING = register("leather_string", Item::new, new Item.Properties());
    public static final Item SILVER_NUGGET = register("silver_nugget", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_NUGGET = register("ancient_metal_nugget", Item::new, new Item.Properties());
    public static final Item MITHRIL_NUGGET = register("mithril_nugget", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_NUGGET = register("adamantium_nugget", Item::new, new Item.Properties());
    public static final Item RAW_SILVER = register("raw_silver", Item::new, new Item.Properties());
    public static final Item RAW_MITHRIL = register("raw_mithril", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_SHARD = register("adamantium_shard", Item::new, new Item.Properties());
    public static final Item SILVER_INGOT = register("silver_ingot", Item::new, new Item.Properties());
    public static final Item ANCIENT_METAL_INGOT = register("ancient_metal_ingot", Item::new, new Item.Properties());
    public static final Item MITHRIL_INGOT = register("mithril_ingot", Item::new, new Item.Properties());
    public static final Item ADAMANTIUM_INGOT = register("adamantium_ingot", Item::new, new Item.Properties());
    public static final Item FLINT_AXE = register("flint_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.FLINT, 4, -3.2F));
    public static final Item FLINT_HATCHET = register("flint_hatchet", Item::new, new Item.Properties().axe(
            ModToolMaterials.FLINT, 3, -3F).durability(5));
    public static final Item FLINT_KNIFE = register("flint_knife", Item::new, new Item.Properties().sword(
            ModToolMaterials.FLINT, 2, -2.4F));
    public static final Item FLINT_SHOVEL = register("flint_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.FLINT, 2, -3F));
    public static final Item SALAD = register("salad", Item::new, new Item.Properties()
            .food(new FoodProperties(1, 1.0F, true))
            .usingConvertsTo(Items.BOWL)
            .stacksTo(4));

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
                    creativeTab.insertAfter(Items.ANCIENT_DEBRIS, ModBlocks.DEEPSLATE_SILVER_ORE.asItem());
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> {
                    creativeTab.prepend(FLINT_AXE);
                    creativeTab.prepend(FLINT_HATCHET);
                    creativeTab.prepend(FLINT_SHOVEL);
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((creativeTab) -> creativeTab.insertAfter(Items.RABBIT_STEW, SALAD));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> {
                    creativeTab.insertBefore(Items.WOODEN_SWORD, FLINT_KNIFE);
                    creativeTab.insertBefore(Items.WOODEN_AXE, FLINT_AXE);
                });
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
}
