package cn.starlight.nightmare.util.player;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeUtil {
    public static void setBaseValue(AttributeInstance attr, double value) {
        if (attr != null) attr.setBaseValue(value);
    }

    public static void setModifier(AttributeInstance attr, Identifier id, double amount) {
        if (attr == null) return;
        attr.removeModifier(id);
        if (amount != 0.0) attr.addOrUpdateTransientModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
}
