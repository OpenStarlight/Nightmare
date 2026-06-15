package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.handler.CapabilityHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    // 使用等级对应的盔甲值上限
    @Inject(method = "getArmorValue", at = @At("RETURN"), cancellable = true)
    private void capPlayerArmorValue(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof Player player) {
            cir.setReturnValue(Math.min(cir.getReturnValue(), CapabilityHandler.getMax(player.experienceLevel)));
        }
    }

    // 玩家伤害减半
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "damage")
    private float reducePlayerDamage(float damage, ServerLevel level, DamageSource source) {
        return source.getEntity() instanceof Player ? damage * 0.5F : damage;
    }
}
