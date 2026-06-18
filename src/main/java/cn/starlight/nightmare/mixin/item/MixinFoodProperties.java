package cn.starlight.nightmare.mixin.item;

import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.modifier.ItemModifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodProperties.class)
public class MixinFoodProperties {

    // 跳过指定物品进食完成的打嗝声，仅保留饮用音效
    @Redirect(method = "onConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 1))
    private void nightmare$skipBurpForDrink(Level level$method, Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        if (ItemModifier.isDrinkingItem(stack)) return;
        level.playSound(except, x, y, z, sound, source, volume, pitch);
    }
}
