package cn.starlight.nightmare.modifier;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Map;
import java.util.Set;

public class ItemModifier {

    public static void initialize() {
        // 移除物品
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, creativeTab) -> {
            creativeTab.getDisplayStacks().removeIf(ItemModifier::isForbiddenItem);
            creativeTab.getSearchTabStacks().removeIf(ItemModifier::isForbiddenItem);
        });

        // 可以吃的种子
        Item[] seeds = new Item[]{
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.COCOA_BEANS
        };
        for (Item item : seeds) {
            DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
                builder.set(DataComponents.FOOD, new FoodProperties(0, 0.5f, true));
                builder.set(DataComponents.CONSUMABLE, Consumable.builder().build());
            }));
        }

        // 回复1饱食度和1饱和度的食物
        Item[] foodsOneHunger = new Item[]{
                Items.SWEET_BERRIES, Items.MELON_SLICE, Items.GLOW_BERRIES, Items.MUTTON, Items.CHICKEN, Items.RABBIT, Items.COD, Items.SALMON, Items.TROPICAL_FISH
        };
        for (Item item : foodsOneHunger) modifyFood(item, 1, 1f);

        // 回复2饱食度和2饱和度的食物
        Item[] foodsTwoHunger = new Item[]{
                Items.APPLE, Items.CHORUS_FRUIT, Items.CARROT, Items.BEEF, Items.PORKCHOP, Items.ROTTEN_FLESH
        };
        for (Item item : foodsTwoHunger) modifyFood(item, 2, 2f);

        // 其他食物
        modifyFood(Items.BAKED_POTATO, 3, 4f);
        modifyFood(Items.COOKED_BEEF, 6, 8f);
        modifyFood(Items.COOKED_PORKCHOP, 6, 8f);
        modifyFood(Items.COOKED_MUTTON, 4, 6f);
        modifyFood(Items.COOKED_RABBIT, 4, 6f);
        modifyFood(Items.COOKED_CHICKEN, 4, 6f);
        modifyFood(Items.COOKED_COD, 4, 6f);
        modifyFood(Items.COOKED_SALMON, 4, 6f);
        modifyFood(Items.BREAD, 4, 4f);
        modifyFood(Items.COOKIE, 2, 2f);
        modifyFood(Items.PUMPKIN_PIE, 6, 6f);
        modifyFood(Items.MUSHROOM_STEW, 4, 6f);
        modifyFood(Items.SUSPICIOUS_STEW, 4, 6f);
        modifyFood(Items.BEETROOT_SOUP, 6, 8f);

        // 调整工具数值
        Map<Item, Double> toolDamage = Map.ofEntries(
                Map.entry(Items.GOLDEN_SWORD, 4.0),
                Map.entry(Items.COPPER_SWORD, 6.0),
                Map.entry(Items.IRON_SWORD, 7.0),
                Map.entry(Items.DIAMOND_SWORD, 9.0),
                Map.entry(Items.NETHERITE_SWORD, 11.0),
                Map.entry(Items.GOLDEN_AXE, 5.0),
                Map.entry(Items.COPPER_AXE, 7.0),
                Map.entry(Items.IRON_AXE, 8.0),
                Map.entry(Items.DIAMOND_AXE, 10.0),
                Map.entry(Items.NETHERITE_AXE, 12.0),
                Map.entry(Items.GOLDEN_PICKAXE, 3.0),
                Map.entry(Items.COPPER_PICKAXE, 5.0),
                Map.entry(Items.IRON_PICKAXE, 6.0),
                Map.entry(Items.DIAMOND_PICKAXE, 8.0),
                Map.entry(Items.NETHERITE_PICKAXE, 10.0),
                Map.entry(Items.GOLDEN_SHOVEL, 3.0),
                Map.entry(Items.COPPER_SHOVEL, 5.0),
                Map.entry(Items.IRON_SHOVEL, 6.0),
                Map.entry(Items.DIAMOND_SHOVEL, 8.0),
                Map.entry(Items.NETHERITE_SHOVEL, 10.0),
                Map.entry(Items.WOODEN_HOE, 1.0),
                Map.entry(Items.STONE_HOE, 1.0),
                Map.entry(Items.GOLDEN_HOE, 1.0),
                Map.entry(Items.COPPER_HOE, 1.0),
                Map.entry(Items.IRON_HOE, 1.0),
                Map.entry(Items.DIAMOND_HOE, 1.0),
                Map.entry(Items.NETHERITE_HOE, 1.0),
                Map.entry(Items.GOLDEN_SPEAR, 1.0),
                Map.entry(Items.COPPER_SPEAR, 3.0),
                Map.entry(Items.IRON_SPEAR, 4.0),
                Map.entry(Items.DIAMOND_SPEAR, 6.0),
                Map.entry(Items.NETHERITE_SPEAR, 8.0)
        );
        for (Map.Entry<Item, Double> entry : toolDamage.entrySet()) {
            DefaultItemComponentEvents.MODIFY.register(context -> context.modify(entry.getKey(), builder -> {
                ItemAttributeModifiers modifiers = entry.getKey().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
                ItemAttributeModifiers.Builder modifierBuilder = ItemAttributeModifiers.builder();
                for (ItemAttributeModifiers.Entry modifierEntry : modifiers.modifiers()) {
                    if (modifierEntry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID)) {
                        modifierBuilder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, entry.getValue() - 1.0, AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                    } else {
                        modifierBuilder.add(modifierEntry.attribute(), modifierEntry.modifier(), modifierEntry.slot(), modifierEntry.display());
                    }
                }
                builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifierBuilder.build());
            }));
        }

        Map<Item, Double> toolSpeed = Map.ofEntries(
                Map.entry(Items.GOLDEN_AXE, 1.0),
                Map.entry(Items.COPPER_AXE, 1.0),
                Map.entry(Items.IRON_AXE, 1.0),
                Map.entry(Items.DIAMOND_AXE, 1.0),
                Map.entry(Items.NETHERITE_AXE, 1.0)
        );

        for (Map.Entry<Item, Double> entry : toolSpeed.entrySet()) {
            DefaultItemComponentEvents.MODIFY.register(context -> context.modify(entry.getKey(), builder -> {
                ItemAttributeModifiers modifiers = entry.getKey().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
                ItemAttributeModifiers.Builder modifierBuilder = ItemAttributeModifiers.builder();
                for (ItemAttributeModifiers.Entry modifierEntry : modifiers.modifiers()) {
                    if (modifierEntry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID)) {
                        modifierBuilder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, entry.getValue() - 4.0, AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                    } else {
                        modifierBuilder.add(modifierEntry.attribute(), modifierEntry.modifier(), modifierEntry.slot(), modifierEntry.display());
                    }
                }
                builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifierBuilder.build());
            }));
        }
    }

    public static boolean isForbiddenItem(ItemStack stack) {
        return stack.is(Items.WOODEN_AXE) || stack.is(Items.WOODEN_PICKAXE) || stack.is(Items.WOODEN_SHOVEL) || stack.is(Items.WOODEN_HOE) || stack.is(Items.WOODEN_SWORD) || stack.is(Items.WOODEN_SPEAR) ||
                stack.is(Items.STONE_PICKAXE) || stack.is(Items.STONE_AXE) || stack.is(Items.STONE_HOE) || stack.is(Items.STONE_SHOVEL) ||
                stack.is(Items.STONE_SPEAR) || stack.is(Items.STONE_SWORD);
    }

    private static void modifyFood(Item item, int hunger, float saturation) {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            builder.set(DataComponents.FOOD, new FoodProperties(hunger, saturation, true));
        }));
    }
}
