package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.world.UndergroundWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpreadingSnowyBlock.class)
public class MixinSpreadingSnowyBlock {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void nightmare$growUndergroundMushrooms(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!state.is(Blocks.MYCELIUM) || !UndergroundWorld.isUnderground(level) || random.nextInt(48) != 0) return;
        BlockPos mushroomPos = pos.above();
        if (!level.getBlockState(mushroomPos).isAir()) return;
        level.setBlock(mushroomPos, random.nextBoolean() ? Blocks.BROWN_MUSHROOM.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState(), 2);
    }
}
