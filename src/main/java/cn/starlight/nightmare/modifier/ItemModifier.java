package cn.starlight.nightmare.modifier;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;

public class ItemModifier {

    public static void initialize() {
        // 可以吃的种子
        Item[] seeds = new Item[] {
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.COCOA_BEANS
        };
        for (Item item : seeds) {
            DefaultItemComponentEvents.MODIFY.register(context -> context.modify(item, builder -> {
                builder.set(DataComponents.FOOD, new FoodProperties(0, 0.5F, true));
                builder.set(DataComponents.CONSUMABLE, Consumable.builder().build());
            }));
        }

        // 调整工具数值
    }

    public static boolean isForbiddenItem(ItemStack stack) {
        return stack.is(Items.WOODEN_PICKAXE) || stack.is(Items.STONE_PICKAXE) || stack.is(Items.STONE_AXE) || stack.is(Items.STONE_HOE) || stack.is(Items.STONE_SHOVEL) ||
                stack.is(Items.STONE_SPEAR) || stack.is(Items.STONE_SWORD);
    }
}
