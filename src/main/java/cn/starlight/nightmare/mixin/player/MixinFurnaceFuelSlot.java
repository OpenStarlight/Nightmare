package cn.starlight.nightmare.mixin.player;

import cn.starlight.nightmare.item.ModBucketBehaviors;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceFuelSlot.class)
public class MixinFurnaceFuelSlot {
    @Inject(method = "isBucket", at = @At("HEAD"), cancellable = true)
    private static void nightmare$acceptCustomEmptyBuckets(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ModBucketBehaviors.isCustomEmptyBucket(stack)) cir.setReturnValue(true);
    }
}
