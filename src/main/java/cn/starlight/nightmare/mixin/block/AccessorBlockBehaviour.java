package cn.starlight.nightmare.mixin.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.class)
public interface AccessorBlockBehaviour {
    @Mutable
    @Accessor("explosionResistance")
    void setExplosionResistance(float explosionResistance);
}
