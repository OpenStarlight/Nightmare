package cn.starlight.nightmare.mixin.inventory;

import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class MixinAbstractContainerMenu {
    @Inject(method = "broadcastChanges", at = @At("HEAD"))
    private void nightmare$tickCraftingProgress(CallbackInfo ci) {
        if ((Object) this instanceof CraftingProgressMenu menu) {
            menu.nightmare$tickCraftingProgress();
        }
    }

    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void nightmare$startTimedCrafting(int slotIndex, int buttonNum, ContainerInput containerInput, net.minecraft.world.entity.player.Player player, CallbackInfo ci) {
        if (slotIndex != 0 || !((Object) this instanceof CraftingProgressMenu menu)) return;
        if (player.level().isClientSide()) {
            if ((containerInput == ContainerInput.PICKUP && buttonNum == 0) || containerInput == ContainerInput.QUICK_MOVE || containerInput == ContainerInput.SWAP) {
                ci.cancel();
            }
            return;
        }

        if (containerInput == ContainerInput.PICKUP && buttonNum == 0) {
            if (menu.nightmare$startCraftingProgress(player, false)) {
                ci.cancel();
            }
        } else if (containerInput == ContainerInput.QUICK_MOVE) {
            if (menu.nightmare$startCraftingProgress(player, true)) {
                ci.cancel();
            }
        } else if (containerInput == ContainerInput.SWAP && buttonNum >= 0 && buttonNum < 9) {
            if (menu.nightmare$startCraftingSwapProgress(player, buttonNum)) {
                ci.cancel();
            }
        } else if (menu.nightmare$isCraftingInProgress() || menu.nightmare$hasTimedCraftingResult()) {
            ci.cancel();
        }
    }
}
