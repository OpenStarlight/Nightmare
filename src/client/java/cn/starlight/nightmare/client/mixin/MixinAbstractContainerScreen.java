package cn.starlight.nightmare.client.mixin;

import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen {
    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void nightmare$sendCraftingResultClickToServer(Slot slot, int slotIndex, int buttonNum, ContainerInput containerInput, CallbackInfo ci) {
        if (slotIndex != 0) return;

        AbstractContainerMenu menu = ((AbstractContainerScreen<?>) (Object) this).getMenu();
        if (!(menu instanceof CraftingProgressMenu)) return;

        if (containerInput != ContainerInput.PICKUP && containerInput != ContainerInput.QUICK_MOVE && containerInput != ContainerInput.SWAP) return;
        if (containerInput == ContainerInput.PICKUP && buttonNum != 0) return;
        if (containerInput == ContainerInput.SWAP && (buttonNum < 0 || buttonNum >= 9)) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameMode == null || minecraft.player == null) return;

        minecraft.gameMode.handleContainerInput(menu.containerId, slotIndex, buttonNum, containerInput, minecraft.player);
        ci.cancel();
    }
}
