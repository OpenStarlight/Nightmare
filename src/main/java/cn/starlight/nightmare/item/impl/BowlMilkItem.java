package cn.starlight.nightmare.item.impl;

import cn.starlight.nightmare.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;

public class BowlMilkItem extends Item {
    public BowlMilkItem(Properties properties) {
        super(properties);
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.BOWL) || !entity.is(EntityTypes.COW)) return InteractionResult.PASS;

            if (level instanceof ServerLevel serverLevel) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.BOWL_MILK)));
                serverLevel.playSound(null, entity.blockPosition(), SoundEvents.COW_MILK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        });
    }
}
