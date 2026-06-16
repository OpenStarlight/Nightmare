package cn.starlight.nightmare.player;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.util.player.AttributeUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodData;

public class CapabilitySystem {
    private static final ThreadLocal<ServerPlayer> CURRENT_FOOD_PLAYER = new ThreadLocal<>();
    private static final Identifier LEVEL_BREAK_SPEED_ID = Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "level_break_speed");
    private static final Identifier LEVEL_ATTACK_DAMAGE_ID = Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "level_attack_damage");

    public static int getMax(int level) {
        return Math.min(6 + (level / 5) * 2, 20);
    }

    public static int getMaxAir(int level) {
        return getMax(level) * 30;
    }

    public static ThreadLocal<ServerPlayer> getCurrentFoodPlayer() { return CURRENT_FOOD_PLAYER; }

    public static void handleAir(ServerPlayer player) {
        int maxAir = getMaxAir(player.experienceLevel);
        if (player.getAirSupply() > maxAir) player.setAirSupply(maxAir);
    }

    public static void handleFood(ServerPlayer player) {
        int cap = getMax(player.experienceLevel);
        FoodData food = player.getFoodData();
        if (food.getFoodLevel() > cap) food.setFoodLevel(cap);
        if (food.getSaturationLevel() > food.getFoodLevel()) food.setSaturation(food.getFoodLevel());
    }

    public static void handleHealth(ServerPlayer player) {
        int cap = getMax(player.experienceLevel);
        AttributeUtil.setBaseValue(player.getAttribute(Attributes.MAX_HEALTH), cap);
        player.setHealth(Math.min(player.getHealth(), (float) cap));
    }

    public static void handleInteractionRange(ServerPlayer player) {
        double entityRange = player.isCreative() ? 4.0 : 2.0;
        double blockRange = player.isCreative() ? 5.0 : 2.5;
        if (player.isShiftKeyDown()) {
            entityRange += 0.5;
            blockRange += 0.5;
        }
        if (player.experienceLevel >= 15) {
            entityRange += 0.5;
            blockRange += 0.5;
        }
        if (player.experienceLevel >= 30) {
            entityRange += 0.5;
            blockRange += 0.5;
        }
        AttributeUtil.setBaseValue(player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), entityRange);
        AttributeUtil.setBaseValue(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), blockRange);
    }

    public static void handleLevelBonuses(ServerPlayer player) {
        int level = Math.min(player.experienceLevel, 100);
        AttributeUtil.setModifier(player.getAttribute(Attributes.BLOCK_BREAK_SPEED), LEVEL_BREAK_SPEED_ID, level * 0.02);
        AttributeUtil.setModifier(player.getAttribute(Attributes.ATTACK_DAMAGE), LEVEL_ATTACK_DAMAGE_ID, level * 0.005);
    }
}
