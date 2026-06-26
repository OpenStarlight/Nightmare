package cn.starlight.nightmare.mixin.block;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.FallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FallingBlock.class)
public interface AccessorFallingBlock {
    @Invoker("falling")
    void nightmare$falling(FallingBlockEntity fallingBlockEntity);
}
