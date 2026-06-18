package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.player.CapabilitySystem;
import cn.starlight.nightmare.player.NutritionSystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public class MixinConsumable {

    @Inject(method = "onConsume", at = @At("HEAD"))
    private void nightmare$consumeStart(net.minecraft.world.level.Level level, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof ServerPlayer player) {
            CapabilitySystem.getCurrentFoodPlayer().set(player);
        }
    }

    @Inject(method = "onConsume", at = @At("RETURN"))
    private void nightmare$consumeEnd(net.minecraft.world.level.Level level, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof ServerPlayer) {
            CapabilitySystem.getCurrentFoodPlayer().remove();
            NutritionSystem.onPlayerConsume((ServerPlayer) user, stack.getItem());
        }
    }
}
