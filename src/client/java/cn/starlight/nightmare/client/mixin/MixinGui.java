package cn.starlight.nightmare.client.mixin;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.player.CapabilitySystem;
import cn.starlight.nightmare.player.NutritionSystem;
import cn.starlight.nightmare.util.DebugFields;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class MixinGui {
    @Unique
    private static final Identifier NIGHTMARE_LEVEL_BREAK_SPEED_ID = Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "level_break_speed");

    @ModifyConstant(method = "extractArmor", constant = @Constant(intValue = 10, ordinal = 1))
    private static int nightmare$modifyArmorSlotCount(int constant, GuiGraphicsExtractor graphics, Player player, int y, int rows, int rowHeight, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractFood", constant = @Constant(intValue = 10))
    private int nightmare$modifyFoodSlotCount(int constant, GuiGraphicsExtractor graphics, Player player, int y, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 0))
    private int nightmare$modifyAirBubbleBaseCount(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 1))
    private int nightmare$modifyAirBubbleLoopCount(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 2))
    private int nightmare$modifyAirBubbleEmptyThreshold(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 3))
    private int nightmare$modifyAirBubbleShakeThreshold(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilitySystem.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "getCurrentAirSupplyBubble", constant = @Constant(intValue = 10))
    private static int nightmare$modifyAirBubbleScale(int constant, int currentAirSupplyTicks, int maxAirSupplyTicks, int tickOffset) {
        return Math.max(1, maxAirSupplyTicks / 30);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void nightmare$drawNightmareDebug(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!NightmareMod.debug || DebugFields.disablePlayerInformation) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null) return;

        String text = "Nightmare [" + minecraft.getFps() + " FPS]";
        int x = 4;
        for (int i = 0; i < text.length(); i++) {
            graphics.text(minecraft.font, String.valueOf(text.charAt(i)), x, 4, nightmare$gradient(i));
            x += minecraft.font.width(String.valueOf(text.charAt(i)));
        }

        long dayTime = minecraft.level.getOverworldClockTime();
        long days = dayTime / 24000L;
        long dayTicks = dayTime % 24000L;
        int cap = CapabilitySystem.getMax(player.experienceLevel);
        double entityRange = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        double blockRange = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        double breakSpeedBonus = nightmare$getModifierAmount(player.getAttribute(Attributes.BLOCK_BREAK_SPEED), NIGHTMARE_LEVEL_BREAK_SPEED_ID) * 100.0D;
        double attackDamageBonus = Math.min(player.experienceLevel, 100) * 0.5D;

        graphics.text(minecraft.font, "Days: " + days + ", " + dayTicks, 4, 14, 0xFFFFFFFF);
        graphics.text(minecraft.font, String.format("Nutrition: %.0f, %.0f, %.0f",
                nightmare$getAttributeValue(player, NutritionSystem.PROTEIN),
                nightmare$getAttributeValue(player, NutritionSystem.PHYTONUTRIENT),
                nightmare$getAttributeValue(player, NutritionSystem.INSULIN_RESISTANCE)), 4, 24, 0xFFFFFFFF);
        graphics.text(minecraft.font, String.format("Capability: %d, (%.1f, %.1f), (%.0f%%, %.1f%%)",
                cap, entityRange, blockRange, breakSpeedBonus, attackDamageBonus), 4, 34, 0xFFFFFFFF);
    }

    @Unique
    private static double nightmare$getAttributeValue(Player player, Holder<Attribute> attribute) {
        AttributeInstance instance = player.getAttribute(attribute);
        return instance == null ? 0.0D : instance.getBaseValue();
    }

    @Unique
    private static double nightmare$getModifierAmount(AttributeInstance instance, Identifier id) {
        if (instance == null) return 0.0D;
        AttributeModifier modifier = instance.getModifier(id);
        return modifier == null ? 0.0D : modifier.amount();
    }

    @Unique
    private static int nightmare$gradient(int index) {
        double wave = Math.sin(System.currentTimeMillis() / 180.0D - index * 0.2D) * 0.5D + 0.5D;
        int r = (int) (24 + wave * 40);
        int g = (int) (96 + wave * 96);
        int b = (int) (180 + wave * 75);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
}
