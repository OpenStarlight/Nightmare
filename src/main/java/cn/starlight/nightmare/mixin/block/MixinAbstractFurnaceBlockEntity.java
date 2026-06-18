package cn.starlight.nightmare.mixin.block;

import cn.starlight.nightmare.item.ModBucketBehaviors;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity {
    @Shadow protected NonNullList<ItemStack> items;

    @Inject(method = "burn", at = @At("TAIL"))
    private static void nightmare$fillCustomBucketFromWetSponge(NonNullList<ItemStack> items, ItemStack input, ItemStack result, CallbackInfo ci) {
        if (!input.is(Items.WET_SPONGE)) return;
        ItemStack waterBucket = ModBucketBehaviors.waterBucketForEmptyBucket(items.get(1));
        if (!waterBucket.isEmpty()) items.set(1, waterBucket);
    }

    @Inject(method = "canPlaceItem", at = @At("HEAD"), cancellable = true)
    private void nightmare$allowCustomEmptyBucketInFuelSlot(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot != 1 || !ModBucketBehaviors.isCustomEmptyBucket(stack)) return;
        ItemStack current = this.items.get(1);
        cir.setReturnValue(!current.is(Items.BUCKET) && !ModBucketBehaviors.isCustomEmptyBucket(current));
    }

    @Inject(method = "canTakeItemThroughFace", at = @At("HEAD"), cancellable = true)
    private void nightmare$allowCustomBucketExtraction(int slot, ItemStack stack, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (direction == Direction.DOWN && slot == 1 && ModBucketBehaviors.isCustomEmptyOrWaterBucket(stack)) {
            cir.setReturnValue(true);
        }
    }
}
