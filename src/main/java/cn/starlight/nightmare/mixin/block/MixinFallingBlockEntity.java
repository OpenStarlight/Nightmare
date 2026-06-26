package cn.starlight.nightmare.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlockEntity {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;discard()V", ordinal = 3), cancellable = true)
    private void nightmare$breakIncompleteLandingBlock(CallbackInfo ci) {
        FallingBlockEntity fallingBlock = (FallingBlockEntity) (Object) this;
        Entity entity = fallingBlock;
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        BlockPos pos = entity.blockPosition();
        BlockState targetState = serverLevel.getBlockState(pos);
        if (targetState.isAir() || targetState.is(Blocks.MOVING_PISTON) || !targetState.getFluidState().isEmpty()) return;
        if (Block.isShapeFullBlock(targetState.getCollisionShape(serverLevel, pos))) return;

        if (serverLevel.destroyBlock(pos, true, fallingBlock, 512)) {
            Vec3 movement = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(movement.x, Math.min(movement.y, -0.1D), movement.z));
            ci.cancel();
        }
    }
}
