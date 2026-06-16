package cn.starlight.nightmare.modifier;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.util.item.ItemUtil;
import cn.starlight.nightmare.util.item.ToolLevel;
import cn.starlight.nightmare.util.world.BlockUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class BlockModifier {
    private static final Map<Block, ToolRequirement> BLOCK_TOOL_REQUIREMENTS = new HashMap<>();
    private static final TagKey<Block> NEEDS_FLINT_AXE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "needs_flint_axe"));
    private static final TagKey<Block> NEEDS_IRON_AXE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "needs_iron_axe"));

    public static void initialize() {
        BlockUtil.modifyBlockProperties(Blocks.ANCIENT_DEBRIS, 15.0F, 500.0F);
        BlockUtil.modifyBlockProperties(Blocks.NETHERITE_BLOCK, 25.0F, 500.0F);
    }

    public static void addToolRequirement(Block block, TagKey<Item> tagKey, ToolLevel toolLevel) {
        BLOCK_TOOL_REQUIREMENTS.put(block, new ToolRequirement(tagKey, toolLevel));
    }

    public static Boolean isCorrectToolForDrops(Block block, ItemStack stack) {
        if (block.defaultBlockState().is(NEEDS_IRON_AXE)) return isCorrectTool(stack, ItemTags.AXES, ToolLevel.IRON);
        if (block.defaultBlockState().is(NEEDS_FLINT_AXE)) return isCorrectTool(stack, ItemTags.AXES, ToolLevel.FLINT);

        ToolRequirement requirement = BLOCK_TOOL_REQUIREMENTS.get(block);
        if (requirement == null) return null;
        return isCorrectTool(stack, requirement.toolType(), requirement.minLevel());
    }

    private static boolean isCorrectTool(ItemStack stack, TagKey<Item> toolType, ToolLevel toolLevel) {
        if (!stack.is(toolType)) return false;
        return ItemUtil.getToolLevel(stack) >= toolLevel.level();
    }

    public record ToolRequirement(TagKey<Item> toolType, ToolLevel minLevel) {
    }
}
