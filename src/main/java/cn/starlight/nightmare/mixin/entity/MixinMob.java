package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.util.mixin.PanicPropagationMob;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMob implements PanicPropagationMob {
    @Shadow
    @Final
    protected GoalSelector goalSelector;

    @Unique
    private static boolean nightmare$propagatingAlert;
    @Unique
    private int nightmare$propagatedPanicTicks;
    @Unique
    private boolean nightmare$wasPanicking;

    @Inject(method = "populateDefaultEquipmentEnchantments", at = @At("HEAD"))
    private void nightmare$addMetalSpawnEquipment(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        Mob self = (Mob)(Object)this;
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        Item[] copper = { Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS };
        Item[] rustedIron = { ModItems.RUSTED_IRON_HELMET, ModItems.RUSTED_IRON_CHESTPLATE, ModItems.RUSTED_IRON_LEGGINGS, ModItems.RUSTED_IRON_BOOTS };
        Item[] silver = { ModItems.SILVER_HELMET, ModItems.SILVER_CHESTPLATE, ModItems.SILVER_LEGGINGS, ModItems.SILVER_BOOTS };
        for (int index = 0; index < slots.length; index++) {
            if (!self.getItemBySlot(slots[index]).is(copper[index])) continue;
            float roll = random.nextFloat();
            if (roll < 0.375F) self.setItemSlot(slots[index], new ItemStack(rustedIron[index]));
            else if (roll < 0.6875F) self.setItemSlot(slots[index], new ItemStack(silver[index]));
        }

        if (!(self instanceof Zombie) || !self.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return;
        float roll = random.nextFloat();
        float multiplier = Math.min(difficulty.getSpecialMultiplier(), 1.0F);
        if (roll >= 0.11F * multiplier) return;

        boolean isRustedIron = roll < 0.06F * multiplier;
        Item tool = switch (random.nextInt(4)) {
            case 0 -> isRustedIron ? ModItems.RUSTED_IRON_SWORD : ModItems.SILVER_SWORD;
            case 1 -> isRustedIron ? ModItems.RUSTED_IRON_AXE : ModItems.SILVER_AXE;
            case 2 -> isRustedIron ? ModItems.RUSTED_IRON_PICKAXE : ModItems.SILVER_PICKAXE;
            default -> isRustedIron ? ModItems.RUSTED_IRON_SHOVEL : ModItems.SILVER_SHOVEL;
        };
        self.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(tool));
    }

    @Inject(method = "setTarget", at = @At("TAIL"))
    private void nightmare$propagatePlayerAlert(LivingEntity target, CallbackInfo ci) {
        Mob self = (Mob)(Object)this;
        if (nightmare$propagatingAlert || !(self instanceof Enemy) || !(target instanceof Player player) || self.getTarget() != player || self.level().isClientSide()) return;

        nightmare$propagatingAlert = true;
        try {
            for (Mob nearby : self.level().getEntitiesOfClass(Mob.class, self.getBoundingBox().inflate(8.0D), mob -> mob != self && mob instanceof Enemy && mob.getTarget() == null && mob.canAttack(player))) {
                nearby.setTarget(player);
            }
        } finally {
            nightmare$propagatingAlert = false;
        }
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void nightmare$propagatePanic(CallbackInfo ci) {
        Mob self = (Mob)(Object)this;
        if (!(self instanceof PathfinderMob pathfinder) || self.level().isClientSide()) return;
        if (this.nightmare$propagatedPanicTicks > 0) this.nightmare$propagatedPanicTicks--;

        boolean panicking = pathfinder.isPanicking();
        if (panicking && !this.nightmare$wasPanicking) {
            for (PathfinderMob nearby : self.level().getEntitiesOfClass(PathfinderMob.class, self.getBoundingBox().inflate(8.0D), mob -> mob != self && !mob.isPanicking() && ((PanicPropagationMob)mob).nightmare$canReceivePanic())) {
                ((PanicPropagationMob)nearby).nightmare$startPanicPropagation();
            }
        }
        this.nightmare$wasPanicking = panicking;
    }

    @Override
    public boolean nightmare$canReceivePanic() {
        Mob self = (Mob)(Object)this;
        if (!(self instanceof PathfinderMob pathfinder)) return false;
        return !pathfinder.getBrain().isBrainDead() || this.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).anyMatch(PanicGoal.class::isInstance);
    }

    @Override
    public void nightmare$startPanicPropagation() {
        Mob self = (Mob)(Object)this;
        if (!(self instanceof PathfinderMob pathfinder) || !this.nightmare$canReceivePanic()) return;
        this.nightmare$propagatedPanicTicks = 100;
        this.nightmare$wasPanicking = true;
        pathfinder.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_PANICKING, true, 100L);
    }

    @Override
    public boolean nightmare$isPanicPropagationActive() {
        return this.nightmare$propagatedPanicTicks > 0;
    }
}
