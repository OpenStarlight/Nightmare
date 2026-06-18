package cn.starlight.nightmare.util.item;

import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.mixin.item.AccessorItemProperties;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ItemUtil {
    public static int getToolLevel(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ModItems.FLINT_AXE || item == ModItems.FLINT_HATCHET || item == ModItems.FLINT_SHOVEL) return ToolLevel.FLINT.level();
        if (item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE) return ToolLevel.WOOD.level();
        if (item == Items.GOLDEN_AXE || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_HOE) return ToolLevel.GOLD.level();
        if (item == Items.COPPER_AXE || item == Items.COPPER_PICKAXE || item == Items.COPPER_SHOVEL || item == Items.COPPER_HOE) return ToolLevel.COPPER.level();
        if (item == ModItems.OBSIDIAN_AXE || item == ModItems.OBSIDIAN_SHOVEL || item == ModItems.SILVER_AXE || item == ModItems.SILVER_PICKAXE || item == ModItems.SILVER_SHOVEL || item == ModItems.SILVER_HOE ||
                item == ModItems.RUSTED_IRON_AXE || item == ModItems.RUSTED_IRON_PICKAXE || item == ModItems.RUSTED_IRON_SHOVEL || item == ModItems.RUSTED_IRON_HOE) return ToolLevel.SILVER.level();
        if (item == Items.IRON_AXE || item == Items.IRON_PICKAXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE) return ToolLevel.IRON.level();
        if (item == ModItems.ANCIENT_METAL_AXE || item == ModItems.ANCIENT_METAL_PICKAXE || item == ModItems.ANCIENT_METAL_SHOVEL || item == ModItems.ANCIENT_METAL_HOE) return ToolLevel.ANCIENT_METAL.level();
        if (item == Items.DIAMOND_AXE || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE) return ToolLevel.DIAMOND.level();
        if (item == ModItems.MITHRIL_AXE || item == ModItems.MITHRIL_PICKAXE || item == ModItems.MITHRIL_SHOVEL || item == ModItems.MITHRIL_HOE) return ToolLevel.MITHRIL.level();
        if (item == Items.NETHERITE_AXE || item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_SHOVEL || item == Items.NETHERITE_HOE) return ToolLevel.NETHERITE.level();
        if (item == ModItems.ADAMANTIUM_AXE || item == ModItems.ADAMANTIUM_PICKAXE || item == ModItems.ADAMANTIUM_SHOVEL || item == ModItems.ADAMANTIUM_HOE) return ToolLevel.ADAMANTIUM.level();
        return ToolLevel.NONE.level();
    }

    public static void modifyFoodProperties(Item item, int hunger, float saturation) {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            builder.set(DataComponents.FOOD, new FoodProperties(hunger, saturation, true));
        }));
    }

    public static void modifyAttackDamage(Item item, double attackDamage) {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            ItemAttributeModifiers modifiers = builder.build().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            ItemAttributeModifiers.Builder modifierBuilder = ItemAttributeModifiers.builder();
            for (ItemAttributeModifiers.Entry modifierEntry : modifiers.modifiers()) {
                if (modifierEntry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID)) {
                    modifierBuilder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage - 1.0, AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                } else {
                    modifierBuilder.add(modifierEntry.attribute(), modifierEntry.modifier(), modifierEntry.slot(), modifierEntry.display());
                }
            }
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifierBuilder.build());
        }));
    }

    public static void modifyAttackSpeed(Item item, double attackSpeed) {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
            ItemAttributeModifiers modifiers = builder.build().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            ItemAttributeModifiers.Builder modifierBuilder = ItemAttributeModifiers.builder();
            for (ItemAttributeModifiers.Entry modifierEntry : modifiers.modifiers()) {
                if (modifierEntry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID)) {
                    modifierBuilder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed - 4.0, AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                } else {
                    modifierBuilder.add(modifierEntry.attribute(), modifierEntry.modifier(), modifierEntry.slot(), modifierEntry.display());
                }
            }
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifierBuilder.build());
        }));
    }

    public static Item.Properties removeComponent(Item.Properties properties, DataComponentType<?> removedType) {
        AccessorItemProperties accessor = (AccessorItemProperties) properties;
        DataComponentInitializers.Initializer<Item> initializer = accessor.getComponentInitializer();

        accessor.setComponentInitializer((builder, registries, itemKey) -> {
            DataComponentMap.Builder tempBuilder = DataComponentMap.builder();
            initializer.run(tempBuilder, registries, itemKey);

            for (TypedDataComponent<?> component : tempBuilder.build()) {
                if (component.type() != removedType) {
                    addComponent(builder, component);
                }
            }
        });

        return properties;
    }

    public static <T> void addComponent(DataComponentMap.Builder builder, TypedDataComponent<T> component) {
        builder.set(component.type(), component.value());
    }
}
