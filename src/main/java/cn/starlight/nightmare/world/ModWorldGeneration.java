package cn.starlight.nightmare.world;

import cn.starlight.nightmare.NightmareMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldGeneration {
    public static final ResourceKey<PlacedFeature> ORE_SILVER = placedFeature("ore_silver");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL = placedFeature("ore_mithril");
    public static final ResourceKey<PlacedFeature> ORE_MITHRIL_SHALLOW = placedFeature("ore_mithril_shallow");
    public static final ResourceKey<PlacedFeature> ORE_ADAMANTIUM = placedFeature("ore_adamantium");

    public static void initialize() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_MITHRIL_SHALLOW);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_ADAMANTIUM);
    }

    private static ResourceKey<PlacedFeature> placedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, name));
    }
}
