package cn.starlight.nightmare.world;

import cn.starlight.nightmare.NightmareMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldGeneration {
    public static final ResourceKey<PlacedFeature> ORE_SILVER = placedFeature("ore_silver");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL = placedFeature("ore_mithril");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL_SHALLOW = placedFeature("ore_mithril_shallow");
    public static final ResourceKey<PlacedFeature> ORE_ADAMANTIUM = placedFeature("ore_adamantium");
    public static final ResourceKey<Biome> UNDERGROUND_BIOME = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "underground"));
    public static final ResourceKey<PlacedFeature> UNDERGROUND_SILVER = placedFeature("underground_silver");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_MITHRIL = placedFeature("underground_mithril");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_ADAMANTIUM = placedFeature("underground_adamantium");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_COPPER = placedFeature("underground_copper");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_IRON = placedFeature("underground_iron");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_GOLD = placedFeature("underground_gold");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_REDSTONE = placedFeature("underground_redstone");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_LAPIS = placedFeature("underground_lapis");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_DIAMOND = placedFeature("underground_diamond");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_DUNGEON = placedFeature("underground_dungeon");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_MYCELIUM = placedFeature("underground_mycelium");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_HUGE_MUSHROOM = placedFeature("underground_huge_mushroom");
    public static final ResourceKey<PlacedFeature> GLOW_LICHEN = vanillaPlacedFeature("glow_lichen");
    public static final ResourceKey<PlacedFeature> AMETHYST_GEODE = vanillaPlacedFeature("amethyst_geode");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_BERRY_BUSH = placedFeature("patch_blue_berry_bush");
    public static final ResourceKey<PlacedFeature> PATCH_BLUE_BERRY_BUSH_PLAINS = placedFeature("patch_blue_berry_bush_plains");

    public static void initialize() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL_SHALLOW);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_SILVER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_MITHRIL);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_ADAMANTIUM);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_COPPER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_IRON);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_GOLD);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_REDSTONE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_LAPIS);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_ORES, UNDERGROUND_DIAMOND);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, UNDERGROUND_DUNGEON);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_DECORATION, UNDERGROUND_MYCELIUM);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.UNDERGROUND_DECORATION, GLOW_LICHEN);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.LOCAL_MODIFICATIONS, AMETHYST_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(UNDERGROUND_BIOME), GenerationStep.Decoration.VEGETAL_DECORATION, UNDERGROUND_HUGE_MUSHROOM);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.WINDSWEPT_FOREST,
                Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE), GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_BLUE_BERRY_BUSH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS), GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_BLUE_BERRY_BUSH_PLAINS);
    }

    private static ResourceKey<PlacedFeature> placedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
    }

    private static ResourceKey<PlacedFeature> vanillaPlacedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.withDefaultNamespace(name));
    }
}
