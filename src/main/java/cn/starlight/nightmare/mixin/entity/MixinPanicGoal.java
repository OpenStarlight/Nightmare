package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.util.mixin.PanicPropagationMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PanicGoal.class)
public class MixinPanicGoal {
    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(method = "shouldPanic", at = @At("HEAD"), cancellable = true)
    private void nightmare$allowPropagatedPanic(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof PanicPropagationMob panicMob && panicMob.nightmare$isPanicPropagationActive()) {
            cir.setReturnValue(true);
        }
    }
}
