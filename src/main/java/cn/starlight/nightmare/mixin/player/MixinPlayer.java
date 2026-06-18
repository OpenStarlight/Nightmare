package cn.starlight.nightmare.mixin.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Shadow
    public int experienceLevel;
    @Shadow
    private boolean canCriticalAttack(Entity entity) { return false; }
    @Unique
    private boolean nightmare$wasCrit;

    // 总是可以吃东西
    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private void nightmare$alwaysCanEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canCriticalAttack(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean nightmare$trackCrit(Player instance, Entity entity) {
        nightmare$wasCrit = this.canCriticalAttack(entity);
        return nightmare$wasCrit;
    }

    // 暴击消耗翻2倍
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private void nightmare$critExhaustion(Player instance, float amount) {
        instance.causeFoodExhaustion(nightmare$wasCrit ? amount * 2.0F : amount);
    }

    /**
     * @author Stars
     * @reason 使用自定义经验曲线
     */
    @Overwrite
    public int getXpNeededForNextLevel() {
        return 10 * (this.experienceLevel + 1);
    }
}
