package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.util.DebugFields;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {

    // 全局放缓挖掘速度
    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    private void reduceGlobalDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (DebugFields.disableBreakSpeedModifier) return;
        float progress = cir.getReturnValue() * 0.1F;
        // 秒挖掘方块额外减慢速度
        if (state.getDestroySpeed(level, pos) == 0.0F) {
            progress = 0.025F;
        }
        cir.setReturnValue(progress);
    }
}
