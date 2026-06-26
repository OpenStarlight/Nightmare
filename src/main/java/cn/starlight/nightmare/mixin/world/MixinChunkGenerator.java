package cn.starlight.nightmare.mixin.world;

import cn.starlight.nightmare.event.WorldProgressEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {
    @Inject(method = "tryGenerateStructure", at = @At("HEAD"), cancellable = true)
    private void nightmare$preventEarlyVillages(StructureSet.StructureSelectionEntry entry, StructureManager structureManager, RegistryAccess registryAccess,
                                                RandomState randomState, StructureTemplateManager templateManager, long seed, ChunkAccess chunk, ChunkPos chunkPos,
                                                SectionPos sectionPos, ResourceKey<Level> dimension, CallbackInfoReturnable<Boolean> cir) {
        if (WorldProgressEvent.hasIronIngot()) return;
        if (!dimension.equals(Level.OVERWORLD)) return;

        entry.structure().unwrapKey().ifPresent(key -> {
            Identifier id = key.identifier();
            if (id.getNamespace().equals("minecraft") && id.getPath().startsWith("village_")) {
                cir.setReturnValue(false);
            }
        });
    }
}
