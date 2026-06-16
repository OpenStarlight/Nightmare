package cn.starlight.nightmare.block;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.mixin.block.AccessorBlockBehaviour;
import cn.starlight.nightmare.mixin.block.AccessorBlockStateBase;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block SILVER_ORE = register("silver_ore", Block::new, blockProperties(3.0F, 3.0F));
    public static final Block DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", Block::new, blockProperties(4.5F, 3.0F));
    public static final Block MITHRIL_ORE = register("mithril_ore", Block::new, blockProperties(4.5F, 5.0F));
    public static final Block DEEPSLATE_MITHRIL_ORE = register("deepslate_mithril_ore", Block::new, blockProperties(6.0F, 5.0F));
    public static final Block ADAMANTIUM_ORE = register("adamantium_ore", Block::new, blockProperties(20.0F, 1000.0F));
    public static final Block SILVER_BLOCK = register("silver_block", Block::new, blockProperties(5.0F, 6.0F));
    public static final Block ANCIENT_METAL_BLOCK = register("ancient_metal_block", Block::new, blockProperties(6.0F, 8.0F));
    public static final Block MITHRIL_BLOCK = register("mithril_block", Block::new, blockProperties(7.0F, 10.0F));
    public static final Block ADAMANTIUM_BLOCK = register("adamantium_block", Block::new, blockProperties(30.0F, 1000.0F));

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(creativeTab -> {
            creativeTab.insertAfter(Items.DEEPSLATE_COPPER_ORE, SILVER_ORE.asItem());
            creativeTab.insertAfter(SILVER_ORE.asItem(), DEEPSLATE_SILVER_ORE.asItem());
            creativeTab.insertAfter(Items.DEEPSLATE_DIAMOND_ORE, MITHRIL_ORE.asItem());
            creativeTab.insertAfter(MITHRIL_ORE.asItem(), DEEPSLATE_MITHRIL_ORE.asItem());
            creativeTab.insertAfter(Items.ANCIENT_DEBRIS, ADAMANTIUM_ORE.asItem());
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(creativeTab -> {
            creativeTab.insertAfter(Items.IRON_BLOCK, SILVER_BLOCK.asItem());
            creativeTab.insertAfter(SILVER_BLOCK.asItem(), ANCIENT_METAL_BLOCK.asItem());
            creativeTab.insertAfter(ANCIENT_METAL_BLOCK.asItem(), MITHRIL_BLOCK.asItem());
            creativeTab.insertAfter(Items.NETHERITE_BLOCK, ADAMANTIUM_BLOCK.asItem());
        });
    }

    public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
        T block = blockFactory.apply(settings.setId(blockKey));

        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
        Item item = new BlockItem(block, new Item.Properties().setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return block;
    }

    private static BlockBehaviour.Properties blockProperties(float destroyTime, float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).requiresCorrectToolForDrops();
    }
}
