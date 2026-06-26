package cn.starlight.nightmare.util.world;

import cn.starlight.nightmare.mixin.block.AccessorBlockBehaviour;
import cn.starlight.nightmare.mixin.block.AccessorBlockStateBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.IdentityHashMap;
import java.util.Map;

public class BlockUtil {
    private static final Map<Block, BlockFallingRule> FALLING_RULES = new IdentityHashMap<>();

    public static void modifyBlockProperties(Block block, float destroyTime, float explosionResistance) {
        ((AccessorBlockBehaviour) block).setExplosionResistance(explosionResistance);
        block.getStateDefinition().getPossibleStates().forEach(state -> ((AccessorBlockStateBase) state).setDestroySpeed(destroyTime));
    }

    public static void setBlockFallingRule(Block block, BlockFallingRule rule) {
        if (rule == null) {
            FALLING_RULES.remove(block);
            return;
        }
        FALLING_RULES.put(block, rule);
    }

    public static BlockFallingRule getBlockFallingRule(BlockState state) {
        BlockFallingRule rule = FALLING_RULES.get(state.getBlock());
        if (rule != null) return rule;
        return state.getBlock() instanceof FallingBlock ? BlockFallingRule.STRICT_FALLING : BlockFallingRule.CONDITIONAL_FALLING;
    }
}
