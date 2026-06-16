package cn.starlight.nightmare.util.world;

import cn.starlight.nightmare.mixin.block.AccessorBlockBehaviour;
import cn.starlight.nightmare.mixin.block.AccessorBlockStateBase;
import net.minecraft.world.level.block.Block;

public class BlockUtil {
    public static void modifyBlockProperties(Block block, float destroyTime, float explosionResistance) {
        ((AccessorBlockBehaviour) block).setExplosionResistance(explosionResistance);
        block.getStateDefinition().getPossibleStates().forEach(state -> ((AccessorBlockStateBase) state).setDestroySpeed(destroyTime));
    }
}
