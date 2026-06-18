package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.player.CapabilitySystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    // 使用等级对应的盔甲值上限
    @Inject(method = "getArmorValue", at = @At("RETURN"), cancellable = true)
    private void nightmare$capPlayerArmorValue(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof Player player) {
            cir.setReturnValue(Math.min(cir.getReturnValue(), CapabilitySystem.getMax(player.experienceLevel)));
        }
    }

    // 玩家伤害减半
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "damage")
    private float nightmare$reducePlayerDamage(float damage, ServerLevel level, DamageSource source) {
        return source.getEntity() instanceof Player ? damage * 0.5F : damage;
    }

    // 死亡只掉落40%经验
    @Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
    private void nightmare$modifyDeathXpDrop(ServerLevel level, Entity killer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!(self instanceof Player player)) return;
        if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) return;
        int drop = (int)(nightmare$totalXp(player.experienceLevel, player.experienceProgress) * 0.4f);
        if (drop > 0) ExperienceOrb.award(level, player.position(), drop);
        ci.cancel();
    }

    @Unique
    private static int nightmare$totalXp(int level, float progress) {
        return (int)(5 * level * (level + 1) + progress * 10 * (level + 1));
    }
}
