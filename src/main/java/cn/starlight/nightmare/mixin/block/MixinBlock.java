package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.modifier.BlockModifier;
import cn.starlight.nightmare.util.DebugFields;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "playerDestroy", at = @At("HEAD"), cancellable = true)
    private void nightmare$preventDropsWithoutRequiredTool(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (DebugFields.disableToolRequirement) return;
        Boolean result = BlockModifier.isCorrectToolForDrops(state.getBlock(), tool);
        if (result != null && !result) {
            player.awardStat(Stats.BLOCK_MINED.get((Block) (Object) this));
            player.causeFoodExhaustion(0.005F);
            ci.cancel();
        }
    }
}
