package cn.starlight.nightmare.mixin.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {
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

    // 饥饿值为0或血量为1时无法攻击怪物
    @Inject(method = "cannotAttack", at = @At(value = "HEAD"), cancellable = true)
    private void nightmare$cannotAttack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getFoodData().getFoodLevel() <= 0 || serverPlayer.getHealth() <= 1F) cir.setReturnValue(true);
        }
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
