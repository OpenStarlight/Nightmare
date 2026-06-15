package cn.starlight.nightmare.item;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ModToolMaterials {
    public static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_flint_tool"));
    public static final TagKey<Block> INCORRECT_FOR_SILVER_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_silver_tool"));
    public static final TagKey<Block> INCORRECT_FOR_ANCIENT_METAL_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_ancient_metal_tool"));
    public static final TagKey<Block> INCORRECT_FOR_MITHRIL_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_mithril_tool"));
    public static final TagKey<Block> INCORRECT_FOR_ADAMANTIUM_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "incorrect_for_adamantium_tool"));

    public static final TagKey<Item> FLINT_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "flint_tool_materials"));
    public static final TagKey<Item> SILVER_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "silver_tool_materials"));
    public static final TagKey<Item> ANCIENT_METAL_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "ancient_metal_tool_materials"));
    public static final TagKey<Item> MITHRIL_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "mithril_tool_materials"));
    public static final TagKey<Item> ADAMANTIUM_TOOL_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "adamantium_tool_materials"));

    public static final ToolMaterial FLINT = new ToolMaterial(
            INCORRECT_FOR_FLINT_TOOL,
            20,
            3.0F,
            0F,
            1,
            FLINT_TOOL_MATERIALS
    );
    public static final ToolMaterial SILVER = new ToolMaterial(
            INCORRECT_FOR_SILVER_TOOL,
            160,
            6.0F,
            1.5F,
            4,
            SILVER_TOOL_MATERIALS
    );
    public static final ToolMaterial ANCIENT_METAL = new ToolMaterial(
            INCORRECT_FOR_ANCIENT_METAL_TOOL,
            640,
            8.5F,
            2.5F,
            6,
            ANCIENT_METAL_TOOL_MATERIALS
    );
    public static final ToolMaterial MITHRIL = new ToolMaterial(
            INCORRECT_FOR_MITHRIL_TOOL,
            2560,
            9.0F,
            3.5F,
            8,
            MITHRIL_TOOL_MATERIALS
    );
    public static final ToolMaterial ADAMANTIUM = new ToolMaterial(
            INCORRECT_FOR_ADAMANTIUM_TOOL,
            5120,
            14.5F,
            5.5F,
            12,
            ADAMANTIUM_TOOL_MATERIALS
    );
}
