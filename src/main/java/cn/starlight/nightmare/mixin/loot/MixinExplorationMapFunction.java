package cn.starlight.nightmare.mixin.loot;

import cn.starlight.nightmare.config.NightmareConfig;
import cn.starlight.nightmare.util.world.AsyncStructureSearch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplorationMapFunction.class)
public class MixinExplorationMapFunction {
    @Shadow @Final private TagKey<Structure> destination;
    @Shadow @Final private Holder<MapDecorationType> mapDecoration;
    @Shadow @Final private byte zoom;
    @Shadow @Final private int searchRadius;
    @Shadow @Final private boolean skipKnownStructures;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void nightmare$createExplorationMapAsync(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        if (!NightmareConfig.get().server.asyncStructureSearch) return;
        if (!stack.is(Items.MAP)) return;

        Vec3 origin = context.getOptionalParameter(LootContextParams.ORIGIN);
        if (origin == null) return;

        ServerLevel level = context.getLevel();
        BlockPos originPos = BlockPos.containing(origin);
        ItemStack pendingMap = MapItem.create(level, originPos.getX(), originPos.getZ(), this.zoom, true, true);
        cir.setReturnValue(pendingMap);

        AsyncStructureSearch
                .submit(() -> level.findNearestMapStructure(this.destination, originPos, this.searchRadius, this.skipKnownStructures))
                .thenAccept(searchResult -> {
                    if (searchResult.status() != AsyncStructureSearch.Status.SUCCESS) return;
                    BlockPos pos = searchResult.value();
                    if (pos == null) return;
                    level.getServer().execute(() -> {
                        ItemStack targetMap = MapItem.create(level, pos.getX(), pos.getZ(), this.zoom, true, true);
                        MapItem.renderBiomePreviewMap(level, targetMap);
                        MapItemSavedData.addTargetDecoration(targetMap, pos, "+", this.mapDecoration);
                        pendingMap.applyComponents(targetMap.getComponents());
                    });
                });
    }
}
