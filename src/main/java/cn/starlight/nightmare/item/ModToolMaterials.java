package cn.starlight.nightmare.item;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ModToolMaterials {
    public static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_flint_tool"));
    public static final TagKey<Item> FLINT_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "flint_tool_materials"));

    public static final ToolMaterial FLINT = new ToolMaterial(
            INCORRECT_FOR_FLINT_TOOL,
            20,
            1.0F,
            0F,
            2,
            FLINT_TOOL_MATERIALS
    );
}
