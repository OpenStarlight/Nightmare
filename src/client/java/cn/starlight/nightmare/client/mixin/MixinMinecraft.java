package cn.starlight.nightmare.client.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void nightmare$forceTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Nightmare");
    }
}
