package cn.starlight.nightmare.world;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class UndergroundWorld {
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "underground"));
    public static final ResourceKey<NoiseGeneratorSettings> NOISE_SETTINGS = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "underground"));

    public static boolean isUnderground(Level level) {
        return level.dimension().equals(LEVEL);
    }
}
