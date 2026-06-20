package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.modifier.ItemModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void nightmare$preferOffhandShield(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (hand == InteractionHand.MAIN_HAND && ItemModifier.isBlockingTool(player.getItemInHand(hand)) && player.getOffhandItem().is(Items.SHIELD)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
