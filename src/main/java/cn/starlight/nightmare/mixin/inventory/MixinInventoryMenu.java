package cn.starlight.nightmare.mixin.inventory;

import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public class MixinInventoryMenu {
    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void nightmare$cancelCraftingProgress(Container container, CallbackInfo ci) {
        ((CraftingProgressMenu) this).nightmare$cancelCraftingProgress();
    }
}
