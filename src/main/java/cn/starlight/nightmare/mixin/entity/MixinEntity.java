package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.player.CapabilitySystem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    // 使用等级对应的氧气值上限
    @Inject(method = "getMaxAirSupply", at = @At("RETURN"), cancellable = true)
    private void nightmare$scalePlayerAirCapacity(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof Player player) {
            cir.setReturnValue(CapabilitySystem.getMax(player.experienceLevel) * 30);
        }
    }
}
