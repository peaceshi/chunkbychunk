package xyz.immortius.chunkbychunk.fabric;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import xyz.immortius.chunkbychunk.common.world.SkyChunkGenerator;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkConstants;

/**
 * Chunk world present class
 */
public class SkyChunkWorldPreset extends WorldPreset {
    private static final ResourceKey<LevelStem> SKY_CHUNK_GENERATION_LEVEL_STEM = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation(ChunkByChunkConstants.MOD_ID, "skychunkgeneration"));

    private final boolean generateSealedWorld;

    public SkyChunkWorldPreset(String id, boolean generateSealedWorld) {
        super(id);
        this.generateSealedWorld = generateSealedWorld;
    }

    @Override
    public ChunkGenerator generator(RegistryAccess registryAccess, long seed) {
        return new SkyChunkGenerator(WorldGenSettings.makeDefaultOverworld(registryAccess, seed), generateSealedWorld);
    }

    @Override
    public WorldGenSettings create(RegistryAccess registryAccess, long seed, boolean generateStructures, boolean bonusChest) {
        Registry<DimensionType> dimensionTypeRegistry = registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);

        WorldGenSettings worldGenSettings = new WorldGenSettings(seed, generateStructures, bonusChest, WorldGenSettings.withOverworld(registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY), DimensionType.defaultDimensions(registryAccess, seed), this.generator(registryAccess, seed)));
        if (worldGenSettings.dimensions().get(LevelStem.OVERWORLD).generator() instanceof SkyChunkGenerator primeGenerator) {
            WritableRegistry<LevelStem> dimensions = (WritableRegistry<LevelStem>) worldGenSettings.dimensions();
            dimensions.register(SKY_CHUNK_GENERATION_LEVEL_STEM, new LevelStem(dimensionTypeRegistry.getOrCreateHolder(DimensionType.OVERWORLD_LOCATION), primeGenerator.getParent()), Lifecycle.stable());
        }

        return worldGenSettings;
    }

}
