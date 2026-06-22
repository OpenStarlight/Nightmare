package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.world.UndergroundWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomBlock.class)
public class MixinMushroomBlock {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void nightmare$growHugeUndergroundMushrooms(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (UndergroundWorld.isUnderground(level) && random.nextInt(480) == 0) {
            ((MushroomBlock) (Object) this).growMushroom(level, pos, state, random);
        }
    }
}
