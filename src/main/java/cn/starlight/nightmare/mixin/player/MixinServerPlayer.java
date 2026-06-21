package cn.starlight.nightmare.mixin.player;

import cn.starlight.nightmare.player.NutritionSystem;
import cn.starlight.nightmare.util.mixin.NightmarePlayer;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements NightmarePlayer {
    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    private static final String NIGHTMARE_PLAYER_INITIALIZED = "NightmarePlayerInitialized";
    @Unique
    private boolean nightmare$nightmarePlayerInitialized;

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void nightmare$readNutritionInitialized(ValueInput input, CallbackInfo ci) {
        this.nightmare$nightmarePlayerInitialized = input.getBooleanOr(NIGHTMARE_PLAYER_INITIALIZED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void nightmare$writeNutritionInitialized(ValueOutput output, CallbackInfo ci) {
        output.putBoolean(NIGHTMARE_PLAYER_INITIALIZED, this.nightmare$nightmarePlayerInitialized);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void nightmare$restoreNutritionInitialized(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
        this.nightmare$nightmarePlayerInitialized = ((NightmarePlayer) oldPlayer).nightmare$isNightmarePlayerInitialized();
    }

    @Override
    public boolean nightmare$isNightmarePlayerInitialized() {
        return this.nightmare$nightmarePlayerInitialized;
    }

    @Override
    public void nightmare$setNightmarePlayerInitialized(boolean initialized) {
        this.nightmare$nightmarePlayerInitialized = initialized;
    }

    // 疾跑消耗翻1.5倍
    @ModifyConstant(method = "checkMovementStatistics", constant = @Constant(floatValue = 0.1f))
    private float nightmare$doubleSprintExhaustion(float original) {
        return original * 1.5F;
    }

    // 初始化营养系统
    @Inject(method = "tick", at = @At("HEAD"))
    private void nightmare$initializeNutrition(CallbackInfo ci) {
        if (this.nightmare$nightmarePlayerInitialized) return;

        ServerPlayer player = (ServerPlayer) (Object) this;
        AttributeInstance protein = player.getAttribute(NutritionSystem.PROTEIN);
        AttributeInstance phytonutrient = player.getAttribute(NutritionSystem.PHYTONUTRIENT);
        AttributeInstance insulinResistance = player.getAttribute(NutritionSystem.INSULIN_RESISTANCE);
        if (protein == null || phytonutrient == null || insulinResistance == null) {
            player.connection.onDisconnect(new DisconnectionDetails(Component.translatable("message.nightmare.nutrition_tick_failed")));
            return;
        }

        protein.setBaseValue(160000.0);
        phytonutrient.setBaseValue(160000.0);
        insulinResistance.setBaseValue(0.0);
        this.nightmare$nightmarePlayerInitialized = true;
    }
}
