package cn.starlight.nightmare.item;

import cn.starlight.nightmare.mixin.block.AccessorCauldronInteractionDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModBucketBehaviors {
    private static final int EMPTY = 0;
    private static final int WATER = 1;
    private static final int LAVA = 2;
    private static final int POWDER_SNOW = 3;

    private static final Item[][] BUCKETS = {
            { ModItems.COPPER_BUCKET, ModItems.COPPER_WATER_BUCKET, ModItems.COPPER_LAVA_BUCKET, ModItems.COPPER_POWDER_SNOW_BUCKET },
            { ModItems.GOLD_BUCKET, ModItems.GOLD_WATER_BUCKET, ModItems.GOLD_LAVA_BUCKET, ModItems.GOLD_POWDER_SNOW_BUCKET },
            { ModItems.SILVER_BUCKET, ModItems.SILVER_WATER_BUCKET, ModItems.SILVER_LAVA_BUCKET, ModItems.SILVER_POWDER_SNOW_BUCKET },
            { ModItems.ANCIENT_METAL_BUCKET, ModItems.ANCIENT_METAL_WATER_BUCKET, ModItems.ANCIENT_METAL_LAVA_BUCKET, ModItems.ANCIENT_METAL_POWDER_SNOW_BUCKET },
            { ModItems.MITHRIL_BUCKET, ModItems.MITHRIL_WATER_BUCKET, ModItems.MITHRIL_LAVA_BUCKET, ModItems.MITHRIL_POWDER_SNOW_BUCKET },
            { ModItems.ADAMANTIUM_BUCKET, ModItems.ADAMANTIUM_WATER_BUCKET, ModItems.ADAMANTIUM_LAVA_BUCKET, ModItems.ADAMANTIUM_POWDER_SNOW_BUCKET }
    };

    public static void registerCauldronInteractions() {
        for (Item[] bucket : BUCKETS) {
            registerFillFromCauldron(bucket[EMPTY], bucket[WATER], bucket[LAVA], bucket[POWDER_SNOW]);
            registerEmptyIntoCauldron(bucket[EMPTY], bucket[WATER], bucket[LAVA], bucket[POWDER_SNOW]);
        }
    }

    public static boolean isCustomEmptyBucket(ItemStack stack) {
        return bucketIndex(stack, EMPTY) >= 0;
    }

    public static boolean isCustomEmptyOrWaterBucket(ItemStack stack) {
        return bucketIndex(stack, EMPTY) >= 0 || bucketIndex(stack, WATER) >= 0;
    }

    public static ItemStack waterBucketForEmptyBucket(ItemStack stack) {
        int index = bucketIndex(stack, EMPTY);
        return index >= 0 ? new ItemStack(BUCKETS[index][WATER]) : ItemStack.EMPTY;
    }

    private static void registerFillFromCauldron(Item emptyBucket, Item waterBucket, Item lavaBucket, Item powderSnowBucket) {
        put(CauldronInteractions.WATER, emptyBucket, (state, level, pos, player, hand, stack) ->
                CauldronInteractions.fillBucket(state, level, pos, player, hand, stack, new ItemStack(waterBucket),
                        checkedState -> checkedState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
        put(CauldronInteractions.LAVA, emptyBucket, (state, level, pos, player, hand, stack) ->
                CauldronInteractions.fillBucket(state, level, pos, player, hand, stack, new ItemStack(lavaBucket),
                        checkedState -> true, SoundEvents.BUCKET_FILL_LAVA));
        put(CauldronInteractions.POWDER_SNOW, emptyBucket, (state, level, pos, player, hand, stack) ->
                CauldronInteractions.fillBucket(state, level, pos, player, hand, stack, new ItemStack(powderSnowBucket),
                        checkedState -> checkedState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
    }

    private static void registerEmptyIntoCauldron(Item emptyBucket, Item waterBucket, Item lavaBucket, Item powderSnowBucket) {
        BlockState waterState = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
        BlockState lavaState = Blocks.LAVA_CAULDRON.defaultBlockState();
        BlockState powderSnowState = Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);

        putFilledBucket(waterBucket, emptyBucket, waterState, SoundEvents.BUCKET_EMPTY);
        putFilledBucket(lavaBucket, emptyBucket, lavaState, SoundEvents.BUCKET_EMPTY_LAVA);
        putFilledBucket(powderSnowBucket, emptyBucket, powderSnowState, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
    }

    private static void putFilledBucket(Item filledBucket, Item emptyBucket, BlockState cauldronState, SoundEvent sound) {
        CauldronInteraction interaction = (state, level, pos, player, hand, stack) -> {
            if (isCustomLavaBucket(filledBucket)) {
                if (level.getFluidState(pos.above()).is(FluidTags.WATER)) return InteractionResult.CONSUME;
            }
            return emptyCustomBucket(level, pos, player, hand, stack, new ItemStack(emptyBucket), cauldronState, sound);
        };

        put(CauldronInteractions.EMPTY, filledBucket, interaction);
        put(CauldronInteractions.WATER, filledBucket, interaction);
        put(CauldronInteractions.LAVA, filledBucket, interaction);
        put(CauldronInteractions.POWDER_SNOW, filledBucket, interaction);
    }

    private static InteractionResult emptyCustomBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack result, BlockState state, SoundEvent sound) {
        if (!level.isClientSide()) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, result));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, state);
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return InteractionResult.SUCCESS;
    }

    private static void put(CauldronInteraction.Dispatcher dispatcher, Item item, CauldronInteraction interaction) {
        ((AccessorCauldronInteractionDispatcher) dispatcher).nightmare$put(item, interaction);
    }

    private static int bucketIndex(ItemStack stack, int content) {
        for (int i = 0; i < BUCKETS.length; i++) {
            if (stack.is(BUCKETS[i][content])) return i;
        }
        return -1;
    }

    private static boolean isCustomLavaBucket(Item item) {
        for (Item[] bucket : BUCKETS) {
            if (item == bucket[LAVA]) return true;
        }
        return false;
    }
}
