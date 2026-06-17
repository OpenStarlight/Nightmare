package cn.starlight.nightmare.player.effect;

import cn.starlight.nightmare.player.effect.impl.DiabetesEffect;
import cn.starlight.nightmare.player.effect.impl.MalnourishedEffect;
import cn.starlight.nightmare.util.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final Holder<MobEffect> MALNOURISHED = RegistryUtil.registerMobEffect("malnourished", new MalnourishedEffect());
    public static final Holder<MobEffect> DIABETES = RegistryUtil.registerMobEffect("diabetes", new DiabetesEffect());

    public static void initialize() {

    }
}
