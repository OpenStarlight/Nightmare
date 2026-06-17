package cn.starlight.nightmare.world;

import cn.starlight.nightmare.NightmareMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldGeneration {
    public static final ResourceKey<PlacedFeature> ORE_SILVER = placedFeature("ore_silver");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL = placedFeature("ore_mithril");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL_SHALLOW = placedFeature("ore_mithril_shallow");
    public static final ResourceKey<PlacedFeature> ORE_ADAMANTIUM = placedFeature("ore_adamantium");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_BERRY_BUSH = placedFeature("patch_blue_berry_bush");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_BERRY_BUSH_PLAINS = placedFeature("patch_blue_berry_bush_plains");

    public static void initialize() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL_SHALLOW);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ADAMANTIUM);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.WINDSWEPT_FOREST,
                Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE), GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_BLUE_BERRY_BUSH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS), GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_BLUE_BERRY_BUSH_PLAINS);
    }

    private static ResourceKey<PlacedFeature> placedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
    }
}
