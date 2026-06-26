package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.world.UndergroundPortalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireChargeItem.class)
public class MixinFireChargeItem {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void nightmare$createUndergroundPortal(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos portalPos = context.getClickedPos().relative(context.getClickedFace());
        if (!UndergroundPortalHandler.tryCreatePortal(level, portalPos)) return;

        float pitch = (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F;
        level.playSound(null, portalPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, pitch);
        level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, portalPos);
        context.getItemInHand().shrink(1);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
