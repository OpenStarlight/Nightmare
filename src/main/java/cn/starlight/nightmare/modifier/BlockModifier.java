package cn.starlight.nightmare.modifier;

import java.util.HashMap;
import java.util.Map;

import cn.starlight.nightmare.util.item.ItemUtil;
import cn.starlight.nightmare.util.item.ToolLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockModifier {
    private static final Map<Block, ToolRequirement> BLOCK_TOOL_REQUIREMENTS = new HashMap<>();

    public static void initialize() {
        // 所有木头均不可用手挖掘
        Block[] allWoods = new Block[] {
                Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_PLANKS,
                Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_PLANKS,
                Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_PLANKS,
                Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_PLANKS,
                Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_PLANKS,
                Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS,
                Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_PLANKS,
                Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_PLANKS,
                Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_WOOD, Blocks.PALE_OAK_PLANKS,
                Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_MOSAIC,
                Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_HYPHAE,
                Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_WARPED_HYPHAE,
        };
        for (Block block : allWoods) {
            addToolRequirement(block, ItemTags.AXES, ToolLevel.FLINT);
        }
    }

    public static void addToolRequirement(Block block, TagKey<Item> tagKey, ToolLevel toolLevel) {
        BLOCK_TOOL_REQUIREMENTS.put(block, new ToolRequirement(tagKey, toolLevel));
    }

    public static Boolean isCorrectToolForDrops(Block block, ItemStack stack) {
        ToolRequirement requirement = BLOCK_TOOL_REQUIREMENTS.get(block);
        if (requirement == null) return null;
        if (!stack.is(requirement.toolType())) return false;
        return ItemUtil.getToolLevel(stack) >= requirement.minLevel().level();
    }

    public record ToolRequirement(TagKey<Item> toolType, ToolLevel minLevel) {
    }
}
