package cn.starlight.nightmare.mixin.item;

import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.Properties.class)
public interface AccessorItemProperties {
    @Accessor("componentInitializer")
    DataComponentInitializers.Initializer<Item> getComponentInitializer();

    @Accessor("componentInitializer")
    void setComponentInitializer(DataComponentInitializers.Initializer<Item> componentInitializer);
}
