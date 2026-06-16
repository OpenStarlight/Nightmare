package cn.starlight.nightmare.mixin.player;

import cn.starlight.nightmare.registry.ModAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayerAttributes {
    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void addNutritionAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(ModAttributes.PROTEIN)
                .add(ModAttributes.PHYTONUTRIENT)
                .add(ModAttributes.INSULIN_RESISTANCE);
    }
}
