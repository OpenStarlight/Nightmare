package cn.starlight.nightmare.mixin.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.BlockStateBase.class)
public interface AccessorBlockStateBase {
    @Mutable
    @Accessor("destroySpeed")
    void setDestroySpeed(float destroySpeed);
}
