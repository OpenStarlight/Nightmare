package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.event.BlockEvent;
import cn.starlight.nightmare.util.world.BlockFallingRule;
import cn.starlight.nightmare.util.world.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class MixinBlockStateBase {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void nightmare$scheduleFallingBlock(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        BlockState state = (BlockState) (Object) this;
        if (state.isAir() || !state.getFluidState().isEmpty()) return;
        BlockFallingRule rule = BlockUtil.getBlockFallingRule(state);
        if (rule != BlockFallingRule.STABLE && (rule != BlockFallingRule.STRICT_FALLING || !(state.getBlock() instanceof FallingBlock))) {
            BlockEvent.scheduleFallingCheck(level, pos);
        }
    }

    @Inject(method = "updateShape", at = @At("TAIL"))
    private void nightmare$scheduleFallingBlock(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = (BlockState) (Object) this;
        if (state.isAir() || !state.getFluidState().isEmpty()) return;
        BlockFallingRule rule = BlockUtil.getBlockFallingRule(state);
        if (rule != BlockFallingRule.STABLE && (rule != BlockFallingRule.STRICT_FALLING || !(state.getBlock() instanceof FallingBlock))) {
            BlockEvent.scheduleFallingCheck(level, pos);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void nightmare$fallingBlockTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockState state = (BlockState) (Object) this;
        if (state.getBlock() instanceof FallingBlock && BlockUtil.getBlockFallingRule(state) != BlockFallingRule.STRICT_FALLING) {
            ci.cancel();
        }
    }
}
