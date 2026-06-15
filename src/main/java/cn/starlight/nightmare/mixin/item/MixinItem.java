package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.modifier.BlockModifier;
import cn.starlight.nightmare.util.DebugFields;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    // 覆盖原版正确工具掉落判定
    @Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    private void applyCustomToolRequirements(ItemStack itemStack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (DebugFields.disableToolRequirement) return;
        Boolean result = BlockModifier.isCorrectToolForDrops(state.getBlock(), itemStack);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
