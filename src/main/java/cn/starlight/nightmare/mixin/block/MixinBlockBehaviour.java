package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.util.DebugFields;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {

    // 全局放缓挖掘速度
    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    private void nightmare$reduceGlobalDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (DebugFields.disableBreakSpeedModifier) return;
        float progress = cir.getReturnValue() * 0.1F;
        // 秒挖掘方块额外减慢速度
        if (state.getDestroySpeed(level, pos) == 0.0F) {
            progress = 0.025F;
        }
        cir.setReturnValue(progress);
    }

    // 使更多方块可以被雨熄灭
    @Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
    private void nightmare$makeLitBlocksRandomlyTick(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (isRainExtinguishable(state)) cir.setReturnValue(true);
    }

    // 使更多方块可以被雨熄灭
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void nightmare$extinguishLitBlocksInRain(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!isRainExtinguishable(state) || !level.isRainingAt(pos) || random.nextFloat() >= 0.2F) return;

        if (isLitCampfire(state)) {
            CampfireBlock.dowse(null, level, pos, state);
            level.setBlock(pos, state.setValue(CampfireBlock.LIT, false), 3);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            return;
        }

        AbstractCandleBlock.extinguish(null, state, level, pos);
    }

    @Unique
    private static boolean isRainExtinguishable(BlockState state) {
        return isLitCampfire(state) || isLitCandle(state);
    }

    @Unique
    private static boolean isLitCampfire(BlockState state) {
        return state.getBlock() instanceof CampfireBlock && state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT);
    }

    @Unique
    private static boolean isLitCandle(BlockState state) {
        return (state.getBlock() instanceof CandleBlock || state.getBlock() instanceof CandleCakeBlock) && state.hasProperty(AbstractCandleBlock.LIT) && state.getValue(AbstractCandleBlock.LIT);
    }
}
