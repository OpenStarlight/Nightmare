package cn.starlight.nightmare.registry;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final Holder<Attribute> PROTEIN = register("protein", new RangedAttribute("attribute.name.nightmare.protein", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> PHYTONUTRIENT = register("phytonutrient", new RangedAttribute("attribute.name.nightmare.phytonutrient", 160000.0, 0.0, 160000.0).setSyncable(true));
    public static final Holder<Attribute> INSULIN_RESISTANCE = register("insulin_resistance", new RangedAttribute("attribute.name.nightmare.insulin_resistance", 0.0, 0.0, 160000.0).setSyncable(true));

    private static Holder<Attribute> register(String name, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name), attribute);
    }

    public static void initialize() {
    }
}
