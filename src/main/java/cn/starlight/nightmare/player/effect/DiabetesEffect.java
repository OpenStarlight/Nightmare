package cn.starlight.nightmare.player.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public class DiabetesEffect extends MobEffect {
    protected DiabetesEffect() {
        super(MobEffectCategory.HARMFUL, 0xffffff);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return super.shouldApplyEffectTickThisTick(duration, amplifier);
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel level, @NonNull LivingEntity entity, int amplifier) {
        return super.applyEffectTick(level, entity, amplifier);
    }
}