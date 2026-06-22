package cn.starlight.nightmare.mixin.world;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.world.UndergroundWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class MixinNoiseBasedChunkGenerator {
    @Shadow
    public abstract boolean stable(ResourceKey<NoiseGeneratorSettings> expectedPreset);

    @Inject(method = "fillFromNoise", at = @At("RETURN"), cancellable = true)
    private void nightmare$addUndergroundStrata(net.minecraft.world.level.levelgen.blending.Blender blender, net.minecraft.world.level.levelgen.RandomState randomState,
                                                net.minecraft.world.level.StructureManager structureManager, ChunkAccess centerChunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        boolean underground = stable(UndergroundWorld.NOISE_SETTINGS);
        if (!underground) return;

        cir.setReturnValue(cir.getReturnValue().thenApply(generated -> {
            nightmare$removeSmallFloatingTerrain(generated);
            nightmare$thinWaterfalls(generated);

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            int minX = generated.getPos().getMinBlockX();
            int minZ = generated.getPos().getMinBlockZ();
            for (int x = minX; x < minX + 16; x++) {
                for (int z = minZ; z < minZ + 16; z++) {
                    for (int y = generated.getMinY(); y < 64; y++) {
                        if (generated.getBlockState(pos.set(x, y, z)).is(Blocks.STONE)) {
                            generated.setBlockState(pos, Blocks.DEEPSLATE.defaultBlockState());
                        }
                    }
                }
            }
            return generated;
        }));
    }

    @Unique
    private static void nightmare$thinWaterfalls(ChunkAccess chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int minY = chunk.getMinY();
        int maxY = minY + chunk.getHeight();

        for (int x = minX; x < minX + 16; x++) {
            for (int z = minZ; z < minZ + 16; z++) {
                for (int y = minY + 1; y < maxY; y++) {
                    pos.set(x, y, z);
                    below.set(x, y - 1, z);
                    if (!chunk.getBlockState(pos).is(Blocks.WATER) || !chunk.getBlockState(below).isAir()) continue;

                    int sample = x * 73428767 ^ y * 19349663 ^ z * 912931;
                    if (Math.floorMod(sample, 6) != 0) {
                        chunk.setBlockState(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @Unique
    private static void nightmare$removeSmallFloatingTerrain(ChunkAccess chunk) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int minY = chunk.getMinY();
        int height = chunk.getHeight();
        boolean[] checked = new boolean[16 * 16 * height];

        for (int x = minX + 1; x < minX + 15; x++) {
            for (int z = minZ + 1; z < minZ + 15; z++) {
                for (int y = minY + 1; y < minY + height - 1; y++) {
                    int index = (x - minX) * 16 * height + (z - minZ) * height + y - minY;
                    if (checked[index] || !chunk.getBlockState(new BlockPos(x, y, z)).is(Blocks.STONE)) continue;

                    ArrayDeque<BlockPos> pending = new ArrayDeque<>();
                    List<BlockPos> component = new ArrayList<>(12);
                    pending.add(new BlockPos(x, y, z));
                    checked[index] = true;
                    boolean touchesBoundary = false;
                    boolean tooLarge = false;

                    while (!pending.isEmpty()) {
                        BlockPos current = pending.removeFirst();
                        if (!tooLarge) {
                            component.add(current);
                            if (component.size() > 12) {
                                tooLarge = true;
                                component.clear();
                            }
                        }

                        for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
                            int nextX = current.getX() + direction.getStepX();
                            int nextY = current.getY() + direction.getStepY();
                            int nextZ = current.getZ() + direction.getStepZ();
                            if (nextX <= minX || nextX >= minX + 15 || nextZ <= minZ || nextZ >= minZ + 15 || nextY <= minY || nextY >= minY + height - 1) {
                                touchesBoundary = true;
                                continue;
                            }

                            int nextIndex = (nextX - minX) * 16 * height + (nextZ - minZ) * height + nextY - minY;
                            if (checked[nextIndex]) continue;

                            BlockPos next = new BlockPos(nextX, nextY, nextZ);
                            if (!chunk.getBlockState(next).is(Blocks.STONE)) continue;
                            checked[nextIndex] = true;
                            pending.addLast(next);
                        }
                    }

                    if (!tooLarge && !touchesBoundary) {
                        for (BlockPos block : component) {
                            chunk.setBlockState(block, Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V", at = @At("TAIL"))
    private void nightmare$restoreBottomStrata(WorldGenRegion region, net.minecraft.world.level.StructureManager structureManager,
                                                net.minecraft.world.level.levelgen.RandomState randomState, ChunkAccess chunk, CallbackInfo ci) {
        boolean underground = stable(UndergroundWorld.NOISE_SETTINGS);
        boolean nether = stable(NoiseGeneratorSettings.NETHER);
        if (!underground && !nether) return;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        int minY = chunk.getMinY();
        if (underground) {
            for (int x = minX; x < minX + 16; x++) {
                for (int z = minZ; z < minZ + 16; z++) {
                    for (int y = minY; y < minY + 5; y++) {
                        int depth = y - minY;
                        int sample = x * 73428767 ^ z * 912931 ^ y * 19349663;
                        if (depth == 0 || Math.floorMod(sample, 5) < 5 - depth) {
                            chunk.setBlockState(pos.set(x, y, z), ModBlocks.MANTLE.defaultBlockState());
                        }
                    }
                }
            }
            return;
        }

        for (int x = minX; x < minX + 16; x++) {
            for (int z = minZ; z < minZ + 16; z++) {
                for (int y = minY; y < minY + 5; y++) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).is(Blocks.BEDROCK)) {
                        chunk.setBlockState(pos, ModBlocks.CORE.defaultBlockState());
                    }
                }
            }
        }
    }

}
