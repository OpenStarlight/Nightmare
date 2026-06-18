package cn.starlight.nightmare.mixin.block;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CauldronInteraction.Dispatcher.class)
public interface AccessorCauldronInteractionDispatcher {
    @Invoker("put")
    void nightmare$put(Item item, CauldronInteraction interaction);
}
