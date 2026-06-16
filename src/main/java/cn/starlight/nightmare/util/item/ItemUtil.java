package cn.starlight.nightmare.util.item;

import cn.starlight.nightmare.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemUtil {
    public static int getToolLevel(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ModItems.FLINT_AXE || item == ModItems.FLINT_HATCHET || item == ModItems.FLINT_SHOVEL) return ToolLevel.FLINT.level();
        if (item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE) return ToolLevel.WOOD.level();
        if (item == Items.GOLDEN_AXE || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_HOE) return ToolLevel.GOLD.level();
        if (item == Items.COPPER_AXE || item == Items.COPPER_PICKAXE || item == Items.COPPER_SHOVEL || item == Items.COPPER_HOE) return ToolLevel.COPPER.level();
        if (item == Items.IRON_AXE || item == Items.IRON_PICKAXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE) return ToolLevel.IRON.level();
        if (item == Items.DIAMOND_AXE || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE) return ToolLevel.DIAMOND.level();
        if (item == Items.NETHERITE_AXE || item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_SHOVEL || item == Items.NETHERITE_HOE) return ToolLevel.NETHERITE.level();
        return ToolLevel.NONE.level();
    }
}
