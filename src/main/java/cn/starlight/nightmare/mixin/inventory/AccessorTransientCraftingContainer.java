package cn.starlight.nightmare.mixin.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransientCraftingContainer.class)
public interface AccessorTransientCraftingContainer {
    @Accessor("menu")
    AbstractContainerMenu nightmare$getMenu();
}
