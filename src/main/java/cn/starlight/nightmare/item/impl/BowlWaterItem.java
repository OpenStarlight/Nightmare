package cn.starlight.nightmare.item.impl;

import cn.starlight.nightmare.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

public class BowlWaterItem extends Item {
    public BowlWaterItem(Properties properties) {
        super(properties);
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.BOWL)) return InteractionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.WATER_CAULDRON)) {
                if (level instanceof ServerLevel serverLevel) {
                    LayeredCauldronBlock.lowerFillLevel(state, serverLevel, pos);
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.BOWL_WATER)));
                    serverLevel.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    serverLevel.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                }
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.BOWL)) return InteractionResult.PASS;

            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hitResult.getType() != HitResult.Type.BLOCK) return InteractionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            if (!level.getFluidState(pos).isSourceOfType(Fluids.WATER)) return InteractionResult.PASS;

            if (level instanceof ServerLevel serverLevel) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.BOWL_WATER)));
                serverLevel.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.CAULDRON)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1), 3);
                consume(context);
                serverLevel.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.SUCCESS;
        }

        if (state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) < 3) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, state.getValue(LayeredCauldronBlock.LEVEL) + 1), 3);
                consume(context);
                serverLevel.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.SUCCESS;
        }

        if (state.is(Blocks.FARMLAND)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setBlock(pos, state.setValue(FarmlandBlock.MOISTURE, FarmlandBlock.MAX_MOISTURE), 2);
                consume(context);
                serverLevel.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.SUCCESS;
        }

        if (extinguish(level, pos, state, player)) {
            consume(context);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static boolean extinguish(Level level, BlockPos pos, BlockState state, Player player) {
        if (CampfireBlock.isLitCampfire(state)) {
            if (level instanceof ServerLevel serverLevel) {
                CampfireBlock.dowse(player, serverLevel, pos, state);
                serverLevel.setBlock(pos, state.setValue(CampfireBlock.LIT, false), 3);
                serverLevel.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.8F);
                serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return true;
        }

        if ((state.getBlock() instanceof CandleBlock || state.getBlock() instanceof CandleCakeBlock) && AbstractCandleBlock.isLit(state)) {
            if (level instanceof ServerLevel serverLevel) AbstractCandleBlock.extinguish(player, state, serverLevel, pos);
            return true;
        }

        if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.removeBlock(pos, false);
                serverLevel.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.8F);
                serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return true;
        }

        return false;
    }

    private static void consume(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || player.getAbilities().instabuild) return;
        context.getItemInHand().shrink(1);
        player.addItem(new ItemStack(Items.BOWL));
    }
}
