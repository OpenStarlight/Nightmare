package cn.starlight.nightmare.client.mixin;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {

    @ModifyConstant(method = "extractArmor", constant = @Constant(intValue = 10, ordinal = 1))
    private static int modifyArmorSlotCount(int constant, GuiGraphicsExtractor graphics, Player player, int y, int rows, int rowHeight, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractFood", constant = @Constant(intValue = 10))
    private int modifyFoodSlotCount(int constant, GuiGraphicsExtractor graphics, Player player, int y, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 0))
    private int modifyAirBubbleBaseCount(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 1))
    private int modifyAirBubbleLoopCount(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 2))
    private int modifyAirBubbleEmptyThreshold(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "extractAirBubbles", constant = @Constant(intValue = 10, ordinal = 3))
    private int modifyAirBubbleShakeThreshold(int constant, GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int y, int x) {
        return CapabilityHandler.getMax(player.experienceLevel) / 2;
    }

    @ModifyConstant(method = "getCurrentAirSupplyBubble", constant = @Constant(intValue = 10))
    private static int modifyAirBubbleScale(int constant, int currentAirSupplyTicks, int maxAirSupplyTicks, int tickOffset) {
        return Math.max(1, maxAirSupplyTicks / 30);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void drawNightmareDebug(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!NightmareMod.debug) return;
        Minecraft minecraft = Minecraft.getInstance();
        String text = "Nightmare [" + minecraft.getFps() + " FPS]";
        int x = 4;
        for (int i = 0; i < text.length(); i++) {
            graphics.text(minecraft.font, String.valueOf(text.charAt(i)), x, 4, nightmare$gradient(i));
            x += minecraft.font.width(String.valueOf(text.charAt(i)));
        }
    }

    @Unique
    private static int nightmare$gradient(int index) {
        double wave = Math.sin(System.currentTimeMillis() / 180.0D - index * 0.65D) * 0.5D + 0.5D;
        int r = (int) (24 + wave * 40);
        int g = (int) (96 + wave * 96);
        int b = (int) (180 + wave * 75);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
}
