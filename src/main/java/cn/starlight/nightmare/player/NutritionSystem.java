package cn.starlight.nightmare.player;

import cn.starlight.nightmare.player.effect.ModEffects;
import cn.starlight.nightmare.util.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class NutritionSystem {
    public static final Holder<Attribute> PROTEIN = RegistryUtil.registerAttribute("protein", new RangedAttribute(
            "attribute.name.nightmare.protein", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> PHYTONUTRIENT = RegistryUtil.registerAttribute("phytonutrient", new RangedAttribute(
            "attribute.name.nightmare.phytonutrient", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> INSULIN_RESISTANCE = RegistryUtil.registerAttribute("insulin_resistance", new RangedAttribute(
            "attribute.name.nightmare.insulin_resistance", 0.0, 0.0, 160000.0).setSyncable(true));

    public static void tickPlayer(ServerPlayer player) {
        AttributeInstance proteinAttr = player.getAttribute(PROTEIN);
        AttributeInstance phytonutrientAttr = player.getAttribute(PHYTONUTRIENT);
        AttributeInstance insulinResistanceAttr = player.getAttribute(INSULIN_RESISTANCE);
        if (proteinAttr == null || phytonutrientAttr == null || insulinResistanceAttr == null) {
            player.connection.onDisconnect(new DisconnectionDetails(Component.translatable("message.nightmare.nutrition_tick_failed")));
            return;
        }

        double protein = proteinAttr.getBaseValue();
        double phytonutrient = phytonutrientAttr.getValue();
        double insulinResistance = insulinResistanceAttr.getValue();
        // 每一刻减1
        if (protein > 0) protein--;
        if (phytonutrient > 0) phytonutrient--;
        if (insulinResistance > 0) insulinResistance--;
        // 确保非负
        if (protein < 0) protein = 0;
        if (phytonutrient < 0) phytonutrient = 0;
        if (insulinResistance < 0) insulinResistance = 0;

        // 重设值
        proteinAttr.setBaseValue(protein);
        phytonutrientAttr.setBaseValue(phytonutrient);
        insulinResistanceAttr.setBaseValue(insulinResistance);

        // 处理营养不良
        int targetMalnourishedLevel = 0;
        if (protein < 8000) targetMalnourishedLevel++;
        if (phytonutrient < 8000) targetMalnourishedLevel++;

        MobEffectInstance playerMalnourishedEffect = player.getEffect(ModEffects.MALNOURISHED);
        if (playerMalnourishedEffect != null) {
            int playerMalnourishedLevel = playerMalnourishedEffect.getAmplifier();
            if (targetMalnourishedLevel == 0) {
                player.removeEffect(ModEffects.MALNOURISHED);
            }
            else if (targetMalnourishedLevel != playerMalnourishedLevel) {
                MobEffectInstance targetMalnourishedEffect = new MobEffectInstance(ModEffects.MALNOURISHED, 1200, targetMalnourishedLevel - 1, false, false, true);
                player.removeEffect(ModEffects.MALNOURISHED);
                player.addEffect(targetMalnourishedEffect);
            }
        } else if (targetMalnourishedLevel != 0) {
            MobEffectInstance targetMalnourishedEffect = new MobEffectInstance(ModEffects.MALNOURISHED, 1200, targetMalnourishedLevel - 1, false, false, true);
            player.addEffect(targetMalnourishedEffect);
        }

        // 处理糖尿病
        int targetDiabetesLevel = 0;
        if (insulinResistance > 48000) targetDiabetesLevel++;
        if (insulinResistance > 96000) targetDiabetesLevel++;
        if (insulinResistance > 144000) targetDiabetesLevel++;

        MobEffectInstance playerDiabetesEffect = player.getEffect(ModEffects.DIABETES);
        if (playerDiabetesEffect != null) {
            int playerDiabetesLevel = playerDiabetesEffect.getAmplifier();
            if (targetDiabetesLevel == 0) {
                player.removeEffect(ModEffects.DIABETES);
            }
            else if (targetDiabetesLevel != playerDiabetesLevel) {
                MobEffectInstance targetDiabetesEffect = new MobEffectInstance(ModEffects.DIABETES, 1200, targetDiabetesLevel - 1, false, false, true);
                player.removeEffect(ModEffects.DIABETES);
                player.addEffect(targetDiabetesEffect);
            }
        } else if (targetDiabetesLevel != 0) {
            MobEffectInstance targetDiabetesEffect = new MobEffectInstance(ModEffects.DIABETES, 1200, targetDiabetesLevel - 1, false, false, true);
            player.addEffect(targetDiabetesEffect);
        }
    }
}
