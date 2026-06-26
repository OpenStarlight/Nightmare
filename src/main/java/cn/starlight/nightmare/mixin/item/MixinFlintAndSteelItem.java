package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.world.UndergroundPortalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class MixinFlintAndSteelItem {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void nightmare$createUndergroundPortal(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos portalPos = context.getClickedPos().relative(context.getClickedFace());
        if (!UndergroundPortalHandler.tryCreatePortal(level, portalPos)) return;

        Player player = context.getPlayer();
        level.playSound(player, portalPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.gameEvent(player, GameEvent.BLOCK_PLACE, portalPos);
        if (player != null) {
            context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
