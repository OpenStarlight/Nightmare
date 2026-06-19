package cn.starlight.nightmare.client.mixin;

import cn.starlight.nightmare.client.gui.CraftingProgressOverlay;
import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreen.class)
public class MixinCraftingScreen {
    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void nightmare$drawCraftingProgress(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        AccessorAbstractContainerScreen screen = (AccessorAbstractContainerScreen) this;
        CraftingProgressOverlay.draw(graphics, (CraftingProgressMenu) ((CraftingScreen) (Object) this).getMenu(), screen.nightmare$getLeftPos() + 89, screen.nightmare$getTopPos() + 35, 24, 16);
    }
}
