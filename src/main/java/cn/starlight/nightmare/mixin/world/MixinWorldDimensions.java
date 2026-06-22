package cn.starlight.nightmare.mixin.world;

import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldDimensions.class)
public class MixinWorldDimensions {
    @Inject(method = "checkStability", at = @At("HEAD"), cancellable = true)
    private static void nightmare$hideExperimentalWorldWarning(ResourceKey<LevelStem> key, LevelStem stem, CallbackInfoReturnable<Lifecycle> cir) {
        cir.setReturnValue(Lifecycle.stable());
    }
}
