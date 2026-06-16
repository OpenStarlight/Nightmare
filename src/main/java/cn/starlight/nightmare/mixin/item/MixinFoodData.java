package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.system.CapabilitySystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodData.class)
public class MixinFoodData {
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private int tickTimer;

    /**
     * @author Stars
     * @reason 超出上限的营养转为饱和度
     */
    @Overwrite
    public void eat(int nutrition, float saturationModifier) {
        ServerPlayer player = CapabilitySystem.getCurrentFoodPlayer().get();
        int cap = player == null ? 20 : CapabilitySystem.getMax(player.experienceLevel);
        int missing = Math.max(cap - this.foodLevel, 0);
        int appliedNutrition = Math.min(nutrition, missing);
        this.foodLevel = Math.clamp(this.foodLevel + appliedNutrition, 0, cap);
        this.saturationLevel = Math.clamp(this.saturationLevel + FoodConstants.saturationByModifier(nutrition, saturationModifier), 0.0F, this.foodLevel);
    }

    /**
     * @author Stars
     * @reason 使用等级对应的饱食度上限
     */
    @Overwrite
    public boolean needsFood() {
        ServerPlayer player = CapabilitySystem.getCurrentFoodPlayer().get();
        int cap = player == null ? 20 : CapabilitySystem.getMax(player.experienceLevel);
        return this.foodLevel < cap;
    }

    /**
     * @author Stars
     * @reason 饱食度大于0就能冲刺
     */
    @Overwrite
    public boolean hasEnoughFood() {
        return this.foodLevel > 0;
    }

    /**
     * @author Stars
     * @reason 使用自定义自然回血规则，保留饥饿掉血
     */
    @Overwrite
    public void tick(ServerPlayer player) {
        Difficulty difficulty = player.level().getDifficulty();
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if (this.foodLevel > 1 && player.isHurt()) {
            this.tickTimer++;
            if (this.tickTimer >= 1200) {
                player.heal(1.0F);
                if (this.saturationLevel > 0.0F) {
                    this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
                } else {
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
                }
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                float minHealth = player.getMaxHealth() / 2.0F;
                if (player.getHealth() > minHealth || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.hurtServer(player.level(), player.damageSources().starve(), 1.0F);
                }
                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }
}
