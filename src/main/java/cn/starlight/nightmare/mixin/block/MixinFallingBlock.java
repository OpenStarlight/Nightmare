package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.util.world.BlockFallingRule;
import cn.starlight.nightmare.util.world.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class MixinFallingBlock {
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void nightmare$cancelNonStrictFallingDust(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (BlockUtil.getBlockFallingRule(state) != BlockFallingRule.STRICT_FALLING) ci.cancel();
    }
}
