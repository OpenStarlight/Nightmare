package cn.starlight.nightmare.util.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class BucketMelting {
    public static boolean tryMelt(Level level, Player player, InteractionHand hand, BlockPos lavaPos, float chance) {
        if (!level.getFluidState(lavaPos).isSourceOfType(net.minecraft.world.level.material.Fluids.LAVA)) return false;
        if (!(level instanceof ServerLevel serverLevel)) return false;
        if (level.getRandom().nextFloat() >= chance) return false;

        if (!player.hasInfiniteMaterials()) player.getItemInHand(hand).shrink(1);
        serverLevel.setBlock(lavaPos, Blocks.AIR.defaultBlockState(), 3);
        BlockPos pos = player.blockPosition();
        if (serverLevel.getBlockState(pos).canBeReplaced(net.minecraft.world.level.material.Fluids.LAVA)) {
            serverLevel.setBlock(pos, Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, 7), 3);
        }
        serverLevel.playSound(null, lavaPos, SoundEvents.GENERIC_BURN, SoundSource.BLOCKS, 1.0F, 1.0F);
        serverLevel.gameEvent(player, GameEvent.FLUID_PICKUP, lavaPos);
        return true;
    }
}
