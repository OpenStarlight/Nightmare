package cn.starlight.nightmare.mixin.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToolMaterial.class)
public class MixinToolMaterial {
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial WOOD;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial STONE;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial COPPER;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial IRON;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial DIAMOND;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial GOLD;
    @Mutable
    @Shadow
    @Final
    public static ToolMaterial NETHERITE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyToolMaterials(CallbackInfo ci) {
        WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 40, 2.0F, 0.5F, 2, ItemTags.WOODEN_TOOL_MATERIALS);
        STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 40, 2.0F, 0.5F, 2, ItemTags.STONE_TOOL_MATERIALS);
        GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, 80, 3.0F, 1.0F, 3, ItemTags.GOLD_TOOL_MATERIALS);
        COPPER = new ToolMaterial(BlockTags.INCORRECT_FOR_COPPER_TOOL, 160, 4.0F, 1.5F, 4, ItemTags.COPPER_TOOL_MATERIALS);
        IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 320, 5.0F, 2.0F, 5, ItemTags.IRON_TOOL_MATERIALS);
        DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1280, 7.0F, 3.0F, 7, ItemTags.DIAMOND_TOOL_MATERIALS);
        NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3840, 9.5F, 4.5F, 10, ItemTags.NETHERITE_TOOL_MATERIALS);
    }
}
