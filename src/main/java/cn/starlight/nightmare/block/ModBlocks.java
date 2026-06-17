package cn.starlight.nightmare.block;

import cn.starlight.nightmare.util.RegistryUtil;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    // Ores
    public static final Block SILVER_ORE = RegistryUtil.registerBlock("silver_ore", Block::new, blockProperties(3.0F, 3.0F));
    public static final Block DEEPSLATE_SILVER_ORE = RegistryUtil.registerBlock("deepslate_silver_ore", Block::new, blockProperties(4.5F, 3.0F));
    public static final Block MITHRIL_ORE = RegistryUtil.registerBlock("mithril_ore", Block::new, blockProperties(4.5F, 5.0F));
    public static final Block DEEPSLATE_MITHRIL_ORE = RegistryUtil.registerBlock("deepslate_mithril_ore", Block::new, blockProperties(6.0F, 5.0F));
    public static final Block ADAMANTIUM_ORE = RegistryUtil.registerBlock("adamantium_ore", Block::new, blockProperties(20.0F, 1000.0F));

    // Metal blocks
    public static final Block SILVER_BLOCK = RegistryUtil.registerBlock("silver_block", Block::new, blockProperties(5.0F, 6.0F));
    public static final Block ANCIENT_METAL_BLOCK = RegistryUtil.registerBlock("ancient_metal_block", Block::new, blockProperties(6.0F, 8.0F));
    public static final Block MITHRIL_BLOCK = RegistryUtil.registerBlock("mithril_block", Block::new, blockProperties(7.0F, 10.0F));
    public static final Block ADAMANTIUM_BLOCK = RegistryUtil.registerBlock("adamantium_block", Block::new, blockProperties(30.0F, 1000.0F));

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

    private static BlockBehaviour.Properties blockProperties(float destroyTime, float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).requiresCorrectToolForDrops();
    }
}
