package cn.starlight.nightmare.mixin.entity;

import cn.starlight.nightmare.item.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMob {
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
}
