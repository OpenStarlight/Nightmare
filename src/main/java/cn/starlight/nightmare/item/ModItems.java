package cn.starlight.nightmare.item;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.modifier.ItemModifier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ModItems {
    public static final Item FLINT_SHARD = register("flint_shard", Item::new, new Item.Properties());
    public static final Item LEATHER_STRING = register("leather_string", Item::new, new Item.Properties());
    public static final Item FLINT_AXE = register("flint_axe", Item::new, new Item.Properties().axe(
            ModToolMaterials.FLINT, 4, -3.2F));
    public static final Item FLINT_HATCHET = register("flint_hatchet", Item::new, new Item.Properties().axe(
            ModToolMaterials.FLINT, 3, -3F).durability(5));
    public static final Item FLINT_KNIFE = register("flint_knife", Item::new, new Item.Properties().sword(
            ModToolMaterials.FLINT, 2, -2.4F));
    public static final Item FLINT_SHOVEL = register("flint_shovel", Item::new, new Item.Properties().shovel(
            ModToolMaterials.FLINT, 2, -3F));

    public static void initialize() {
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, creativeTab) -> {
            creativeTab.getDisplayStacks().removeIf(ItemModifier::isForbiddenItem);
            creativeTab.getSearchTabStacks().removeIf(ItemModifier::isForbiddenItem);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> {
                    creativeTab.insertBefore(Items.FLINT, FLINT_SHARD);
                    creativeTab.insertBefore(Items.STRING, LEATHER_STRING);
                });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> {
                    creativeTab.prepend(FLINT_AXE);
                    creativeTab.prepend(FLINT_HATCHET);
                    creativeTab.prepend(FLINT_SHOVEL);
                });
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
