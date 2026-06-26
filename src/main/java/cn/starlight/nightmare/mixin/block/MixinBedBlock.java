package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.world.UndergroundWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class MixinBedBlock {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void nightmare$preventUnsafeSleep(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.dimension().equals(Level.NETHER) && !level.dimension().equals(Level.END) && !UndergroundWorld.isUnderground(level)) return;
        if (level instanceof ServerLevel) player.sendOverlayMessage(Component.translatable("message.nightmare.unsafe_bed"));
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
