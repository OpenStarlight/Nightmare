package cn.starlight.nightmare.player.effect;

import cn.starlight.nightmare.util.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final Holder<MobEffect> MALNOURISHED = RegistryUtil.registerMobEffect("malnourished", new MalnourishedEffect());

    public static void initialize() {

    }
}
