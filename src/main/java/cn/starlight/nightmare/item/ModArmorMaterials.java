package cn.starlight.nightmare.item;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class ModArmorMaterials {
    public static final ArmorMaterial RUSTED_IRON = material(10, 2, 3, 3, 2, 4, 0.0F, 0.0F, "rusted_iron", ModToolMaterials.RUSTED_IRON_TOOL_MATERIALS);
    public static final ArmorMaterial SILVER = material(12, 2, 4, 3, 2, 5, 0.0F, 0.0F, "silver", ModToolMaterials.SILVER_TOOL_MATERIALS);
    public static final ArmorMaterial ANCIENT_METAL = material(24, 2, 5, 4, 2, 6, 1.0F, 0.0F, "ancient_metal", ModToolMaterials.ANCIENT_METAL_TOOL_MATERIALS);
    public static final ArmorMaterial MITHRIL = material(45, 3, 6, 6, 3, 8, 2.5F, 0.0F, "mithril", ModToolMaterials.MITHRIL_TOOL_MATERIALS);
    public static final ArmorMaterial ADAMANTIUM = material(64, 3, 7, 7, 3, 12, 4.0F, 0.15F, "adamantium", ModToolMaterials.ADAMANTIUM_TOOL_MATERIALS);

    private static ArmorMaterial material(int durability, int helmet, int chestplate, int leggings, int boots, int enchantmentValue, float toughness, float knockbackResistance, String name, net.minecraft.tags.TagKey<net.minecraft.world.item.Item> repairIngredient) {
        return new ArmorMaterial(
                durability,
                Map.of(
                        ArmorType.HELMET, helmet,
                        ArmorType.CHESTPLATE, chestplate,
                        ArmorType.LEGGINGS, leggings,
                        ArmorType.BOOTS, boots,
                        ArmorType.BODY, 0
                ),
                enchantmentValue,
                SoundEvents.ARMOR_EQUIP_IRON,
                toughness,
                knockbackResistance,
                repairIngredient,
                ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name))
        );
    }
}
