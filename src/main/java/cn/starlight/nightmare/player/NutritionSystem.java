package cn.starlight.nightmare.player;

import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.player.effect.ModEffects;
import cn.starlight.nightmare.util.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class NutritionSystem {
    public static final Holder<Attribute> PROTEIN = RegistryUtil.registerAttribute("protein", new RangedAttribute(
            "attribute.name.nightmare.protein", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> PHYTONUTRIENT = RegistryUtil.registerAttribute("phytonutrient", new RangedAttribute(
            "attribute.name.nightmare.phytonutrient", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> INSULIN_RESISTANCE = RegistryUtil.registerAttribute("insulin_resistance", new RangedAttribute(
            "attribute.name.nightmare.insulin_resistance", 0.0, 0.0, 192000.0).setSyncable(true));

    public static final Map<Item, NutritionValue> FOOD_NUTRITION_MAP = Map.ofEntries(
            entry(Items.APPLE, 0, 8000, 4800),
            entry(Items.PORKCHOP, 32000, 0, 0),
            entry(Items.COOKED_PORKCHOP, 64000, 0, 0),
            entry(Items.BEEF, 32000, 0, 0),
            entry(Items.COOKED_BEEF, 64000, 0, 0),
            entry(Items.CHICKEN, 24000, 0, 0),
            entry(Items.COOKED_CHICKEN, 48000, 0, 0),
            entry(Items.MUTTON, 24000, 0, 0),
            entry(Items.COOKED_MUTTON, 48000, 0, 0),
            entry(Items.RABBIT, 24000, 0, 0),
            entry(Items.COOKED_RABBIT, 48000, 0, 0),
            entry(Items.GOLDEN_APPLE, 0, 8000, 4800),
            entry(Items.ENCHANTED_GOLDEN_APPLE, 0, 8000, 4800),
            entry(Items.COD, 24000, 0, 0),
            entry(Items.SALMON, 24000, 0, 0),
            entry(Items.TROPICAL_FISH, 24000, 0, 0),
            entry(Items.PUFFERFISH, 24000, 0, 0),
            entry(Items.COOKED_COD, 48000, 0, 0),
            entry(Items.COOKED_SALMON, 48000, 0, 0),
            entry(Items.COOKIE, 0, 24000, 1200),
            entry(Items.COCOA_BEANS, 0, 0, 1200),
            entry(Items.SUGAR, 0, 0, 4800),
            entry(Items.MELON_SLICE, 0, 8000, 4800),
            entry(Items.SWEET_BERRIES, 0, 8000, 4800),
            entry(ModItems.BLUE_BERRIES, 0, 8000, 4800),
            entry(Items.GLOW_BERRIES, 0, 8000, 4800),
            entry(Items.CHORUS_FRUIT, 0, 8000, 4800),
            entry(Items.ROTTEN_FLESH, 8000, 0, 0),
            entry(Items.SPIDER_EYE, 8000, 0, 0),
            entry(Items.CARROT, 0, 16000, 0),
            entry(Items.BEETROOT, 0, 16000, 0),
            entry(Items.POTATO, 0, 16000, 0),
            entry(Items.POISONOUS_POTATO, 0, 16000, 0),
            entry(Items.BAKED_POTATO, 0, 16000, 0),
            entry(Items.PUMPKIN_PIE, 16000, 16000, 4800),
            entry(Items.DRIED_KELP, 0, 8000, 0),
            entry(Items.BREAD, 0, 48000, 0),
            entry(Items.BROWN_MUSHROOM, 0, 8000, 0),
            entry(Items.RED_MUSHROOM, 0, 8000, 0),
            entry(Items.MUSHROOM_STEW, 0, 16000, 0),
            entry(Items.BEETROOT_SOUP, 0, 80000, 0),
            entry(Items.RABBIT_STEW, 48000, 40000, 0),
            entry(Items.SUSPICIOUS_STEW, 0, 16000, 0),
            entry(ModItems.SALAD, 0, 8000, 0),
            entry(ModItems.CEREAL, 0, 16000, 4800),
            entry(ModItems.PORRIDGE, 0, 8000, 9600),
            entry(Items.HONEY_BOTTLE, 0, 0, 9600)
    );

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
                MobEffectInstance targetMalnourishedEffect = new MobEffectInstance(ModEffects.MALNOURISHED, 1234, targetMalnourishedLevel - 1, false, false, true);
                player.removeEffect(ModEffects.MALNOURISHED);
                player.addEffect(targetMalnourishedEffect);
            }
        } else if (targetMalnourishedLevel != 0) {
            MobEffectInstance targetMalnourishedEffect = new MobEffectInstance(ModEffects.MALNOURISHED, 1234, targetMalnourishedLevel - 1, false, false, true);
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
                MobEffectInstance targetDiabetesEffect = new MobEffectInstance(ModEffects.DIABETES, 1234, targetDiabetesLevel - 1, false, false, true);
                player.removeEffect(ModEffects.DIABETES);
                player.addEffect(targetDiabetesEffect);
            }
        } else if (targetDiabetesLevel != 0) {
            MobEffectInstance targetDiabetesEffect = new MobEffectInstance(ModEffects.DIABETES, 1234, targetDiabetesLevel - 1, false, false, true);
            player.addEffect(targetDiabetesEffect);
        }
    }

    public static void onPlayerConsume(ServerPlayer player, Item item) {
        AttributeInstance proteinAttr = player.getAttribute(PROTEIN);
        AttributeInstance phytonutrientAttr = player.getAttribute(PHYTONUTRIENT);
        AttributeInstance insulinResistanceAttr = player.getAttribute(INSULIN_RESISTANCE);
        if (proteinAttr == null || phytonutrientAttr == null || insulinResistanceAttr == null) {
            player.connection.onDisconnect(new DisconnectionDetails(Component.translatable("message.nightmare.nutrition_tick_failed")));
            return;
        }

        int insulinResistance = 0;
        for (Map.Entry<Item, NutritionValue> entry : FOOD_NUTRITION_MAP.entrySet()) {
            if (item == entry.getKey()) {
                proteinAttr.setBaseValue(PROTEIN.value().sanitizeValue(proteinAttr.getBaseValue() + entry.getValue().protein));
                phytonutrientAttr.setBaseValue(PHYTONUTRIENT.value().sanitizeValue(phytonutrientAttr.getBaseValue() + entry.getValue().phytonutrient));
                insulinResistanceAttr.setBaseValue(INSULIN_RESISTANCE.value().sanitizeValue(insulinResistanceAttr.getBaseValue() + entry.getValue().insulinResistance));
                insulinResistance = (int) entry.getValue().insulinResistance;
                break;
            }
        }

        if (insulinResistance > 0) {
            MobEffectInstance diabetes = player.getEffect(ModEffects.DIABETES);
            if (diabetes != null) {
                int amplifier = diabetes.getAmplifier();
                player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 400, amplifier, false, true, true));
                if (amplifier >= 2) player.addEffect(new MobEffectInstance(MobEffects.POISON, insulinResistance / 48, amplifier - 2, false, true, true));
            }
        }
    }

    public record NutritionValue(double protein, double phytonutrient, double insulinResistance) {
    }

    private static Map.Entry<Item, NutritionValue> entry(Item item, int protein, int phytonutrient, int insulinResistance) {
        return Map.entry(item, new NutritionValue(protein, phytonutrient, insulinResistance));
    }
}
