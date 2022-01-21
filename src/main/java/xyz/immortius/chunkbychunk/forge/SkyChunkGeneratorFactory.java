package xyz.immortius.chunkbychunk.forge;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraftforge.common.world.ForgeWorldType;
import xyz.immortius.chunkbychunk.common.world.SkyChunkGenerator;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkConstants;

/**
 * Factory for generating a SkyChunk world. Will add an extra dimension for generating the true world to copy into the
 * overworld.
 */
public class SkyChunkGeneratorFactory implements ForgeWorldType.IBasicChunkGeneratorFactory {

    private static final ResourceKey<LevelStem> SKY_CHUNK_GENERATION_LEVEL_STEM = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation(ChunkByChunkConstants.MOD_ID, "skychunkgeneration"));

    private final boolean generateSealedWorld;

    public SkyChunkGeneratorFactory(boolean generateSealedWorld) {
        this.generateSealedWorld = generateSealedWorld;
    }

    public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, long seed) {
        return new SkyChunkGenerator(WorldGenSettings.makeDefaultOverworld(biomeRegistry, dimensionSettingsRegistry, seed), generateSealedWorld);
    }

    @Override
    public WorldGenSettings createSettings(RegistryAccess dynamicRegistries, long seed, boolean generateStructures, boolean bonusChest, String generatorSettings) {
        Registry<Biome> biomeRegistry = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<DimensionType> dimensionTypeRegistry = dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        Registry<NoiseGeneratorSettings> dimensionSettingsRegistry = dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);

        MappedRegistry<LevelStem> levelStems = WorldGenSettings.withOverworld(dimensionTypeRegistry,
                DimensionType.defaultDimensions(dimensionTypeRegistry, biomeRegistry, dimensionSettingsRegistry, seed),
                createChunkGenerator(biomeRegistry, dimensionSettingsRegistry, seed, generatorSettings));
        if (levelStems.get(LevelStem.OVERWORLD).generator() instanceof SkyChunkGenerator primeGenerator) {
            levelStems.register(SKY_CHUNK_GENERATION_LEVEL_STEM, new LevelStem(() -> dimensionTypeRegistry.get(DimensionType.OVERWORLD_LOCATION), primeGenerator.getParent()), Lifecycle.stable());
        }

        return new WorldGenSettings(seed, generateStructures, bonusChest, levelStems);
    }
}
