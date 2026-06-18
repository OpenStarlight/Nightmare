package cn.starlight.nightmare.item.impl;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class PowderSnowBucketItem extends SolidBucketItem {
    private final Item emptyResult;

    public PowderSnowBucketItem(Block block, SoundEvent placeSound, Item emptyResult, Properties properties) {
        super(block, placeSound, properties);
        this.emptyResult = emptyResult;
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        InteractionResult result = super.useOn(context);
        Player player = context.getPlayer();
        if (result.consumesAction() && player != null && !player.hasInfiniteMaterials()) {
            player.setItemInHand(context.getHand(), new ItemStack(this.emptyResult));
        }
        return result;
    }
}
