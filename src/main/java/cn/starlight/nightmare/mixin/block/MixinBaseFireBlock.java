package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.world.UndergroundPortalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class MixinBaseFireBlock {
    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    private void nightmare$createUndergroundPortal(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        if (UndergroundPortalHandler.tryCreatePortal(level, pos)) ci.cancel();
    }
}
