package cn.starlight.nightmare.item.impl;

import cn.starlight.nightmare.NightmareMod;
import cn.starlight.nightmare.util.item.BucketMelting;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

public class CopperBucketItem extends BucketItem {
    private final Item emptyResult;
    private final Identifier waterResult;
    private final Identifier lavaResult;
    private final Identifier milkResult;
    private final Identifier powderSnowResult;
    private final float lavaMeltChance;

    public CopperBucketItem(Fluid fluid, Properties properties) {
        this(fluid, null, null, null, null, null, 0.0F, properties);
    }

    public CopperBucketItem(Fluid fluid, Item emptyResult, Properties properties) {
        this(fluid, emptyResult, null, null, null, null, 0.0F, properties);
    }

    public CopperBucketItem(String material, float lavaMeltChance, Properties properties) {
        this(Fluids.EMPTY, null, bucketId(material, "water"), bucketId(material, "lava"), bucketId(material, "milk"), bucketId(material, "powder_snow"), lavaMeltChance, properties);
    }

    private CopperBucketItem(Fluid fluid, Item emptyResult, Identifier waterResult, Identifier lavaResult, Identifier milkResult, Identifier powderSnowResult, float lavaMeltChance, Properties properties) {
        super(fluid, properties);
        this.emptyResult = emptyResult;
        this.waterResult = waterResult;
        this.lavaResult = lavaResult;
        this.milkResult = milkResult;
        this.powderSnowResult = powderSnowResult;
        this.lavaMeltChance = lavaMeltChance;
    }

    private static Identifier bucketId(String material, String content) {
        return Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, material + "_" + content + "_bucket");
    }

    public static BlockHitResult getSourceHitResult(Level level, Player player) {
        return getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, getFluidContext());
        if (hitResult.getType() == HitResult.Type.MISS) return InteractionResult.PASS;
        if (hitResult.getType() != HitResult.Type.BLOCK) return InteractionResult.PASS;

        if (getContent() != Fluids.EMPTY) {
            InteractionResult result = super.use(level, player, hand);
            if (result instanceof InteractionResult.Success success && this.emptyResult != null) {
                return success.heldItemTransformedTo(ItemUtils.createFilledResult(stack, player, new ItemStack(this.emptyResult)));
            }
            return result;
        }

        BlockState state = level.getBlockState(hitResult.getBlockPos());
        if (!(state.getBlock() instanceof BucketPickup pickup)) return InteractionResult.FAIL;
        if (!level.mayInteract(player, hitResult.getBlockPos())) return InteractionResult.FAIL;

        Fluid fluid = level.getFluidState(hitResult.getBlockPos()).getType();
        if (fluid.is(FluidTags.LAVA) && this.lavaMeltChance > 0.0F && BucketMelting.tryMelt(level, player, hand, hitResult.getBlockPos(), this.lavaMeltChance)) return InteractionResult.SUCCESS;

        ItemStack picked = pickup.pickupBlock(player, level, hitResult.getBlockPos(), state);
        if (picked.isEmpty()) return InteractionResult.FAIL;

        ItemStack filled = nightmare$filledBucket(picked);
        if (filled.isEmpty()) return InteractionResult.FAIL;

        player.awardStat(Stats.ITEM_USED.get(this));
        pickup.getPickupSound().ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
        level.gameEvent(player, GameEvent.FLUID_PICKUP, hitResult.getBlockPos());
        ItemStack result = ItemUtils.createFilledResult(stack, player, filled);
        if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, filled);
        return InteractionResult.SUCCESS.heldItemTransformedTo(result);
    }

    @Override
    public @NonNull InteractionResult interactLivingEntity(@NonNull ItemStack stack, @NonNull Player player, @NonNull LivingEntity entity, @NonNull InteractionHand hand) {
        if (getContent() != Fluids.EMPTY || this.milkResult == null || !(entity instanceof AbstractCow) || entity.isBaby()) return InteractionResult.PASS;
        if (player.level() instanceof ServerLevel) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack result = ItemUtils.createFilledResult(stack, player, resultStack(this.milkResult));
            player.setItemInHand(hand, result);
        }
        return InteractionResult.SUCCESS;
    }

    private ItemStack nightmare$filledBucket(ItemStack vanillaBucket) {
        if (vanillaBucket.is(net.minecraft.world.item.Items.WATER_BUCKET)) return resultStack(this.waterResult);
        if (vanillaBucket.is(net.minecraft.world.item.Items.LAVA_BUCKET)) return resultStack(this.lavaResult);
        if (vanillaBucket.is(net.minecraft.world.item.Items.POWDER_SNOW_BUCKET)) return resultStack(this.powderSnowResult);
        return ItemStack.EMPTY;
    }

    private static ItemStack resultStack(Identifier id) {
        if (id == null) return ItemStack.EMPTY;
        return BuiltInRegistries.ITEM.getOptional(id).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
}
