package cn.starlight.nightmare.mixin.player;

import cn.starlight.nightmare.registry.ModAttributes;
import cn.starlight.nightmare.util.player.NutritionPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerNutrition implements NutritionPlayer {
    @Unique
    private static final String NIGHTMARE_NUTRITION_INITIALIZED = "NightmareNutritionInitialized";
    @Unique
    private boolean nightmare$nutritionInitialized;

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNutritionInitialized(ValueInput input, CallbackInfo ci) {
        this.nightmare$nutritionInitialized = input.getBooleanOr(NIGHTMARE_NUTRITION_INITIALIZED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNutritionInitialized(ValueOutput output, CallbackInfo ci) {
        output.putBoolean(NIGHTMARE_NUTRITION_INITIALIZED, this.nightmare$nutritionInitialized);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void restoreNutritionInitialized(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        this.nightmare$nutritionInitialized = ((NutritionPlayer) oldPlayer).nightmare$isNutritionInitialized();
    }

    @Override
    public boolean nightmare$isNutritionInitialized() {
        return this.nightmare$nutritionInitialized;
    }

    @Override
    public void nightmare$setNutritionInitialized(boolean initialized) {
        this.nightmare$nutritionInitialized = initialized;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void initializeNutrition(CallbackInfo ci) {
        if (this.nightmare$nutritionInitialized) return;

        ServerPlayer player = (ServerPlayer) (Object) this;
        AttributeInstance protein = player.getAttribute(ModAttributes.PROTEIN);
        AttributeInstance phytonutrient = player.getAttribute(ModAttributes.PHYTONUTRIENT);
        AttributeInstance insulinResistance = player.getAttribute(ModAttributes.INSULIN_RESISTANCE);
        if (protein == null || phytonutrient == null || insulinResistance == null) return;

        protein.setBaseValue(160000.0);
        phytonutrient.setBaseValue(160000.0);
        insulinResistance.setBaseValue(0.0);
        this.nightmare$nutritionInitialized = true;
    }
}
