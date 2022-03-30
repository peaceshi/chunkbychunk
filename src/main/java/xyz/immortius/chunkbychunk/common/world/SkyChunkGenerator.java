package xyz.immortius.chunkbychunk.common.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import xyz.immortius.chunkbychunk.fabric.mixins.ChunkGeneratorStructureAccessor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

/**
 * The prime Sky Chunk Generator - this generates the overworld dimension, which is either empty or is
 * a simple heightmap of bedrock based on generateSealedWorld.
 */
public class SkyChunkGenerator extends ChunkGenerator {

    public static final Codec<SkyChunkGenerator> CODEC = RecordCodecBuilder.create((encoded) ->
            encoded.group(ChunkGenerator.CODEC.withLifecycle(Lifecycle.stable()).fieldOf("parent").forGetter((decoded) -> decoded.parent),
                            Codec.BOOL.withLifecycle(Lifecycle.stable()).fieldOf("sealed").forGetter((decoded) -> decoded.generateSealedWorld))
                    .apply(encoded, encoded.stable(SkyChunkGenerator::new))
    );

    private final ChunkGenerator parent;
    private final boolean generateSealedWorld;

    /**
     * @param parent The chunkGenerator this generator is based on
     * @param generateSealedWorld Whether to generate a basic bedrock heightmap or not
     */
    public SkyChunkGenerator(ChunkGenerator parent, boolean generateSealedWorld) {
        super(((ChunkGeneratorStructureAccessor) parent).getStructureSet(), ((ChunkGeneratorStructureAccessor) parent).getStructureOverrides(), parent.getBiomeSource());
        this.parent = parent;
        this.generateSealedWorld = generateSealedWorld;
    }

    public ChunkGenerator getParent() {
        return parent;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return SkyChunkGenerator.CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new SkyChunkGenerator(parent.withSeed(seed), generateSealedWorld);
    }

    @Override
    public Climate.Sampler climateSampler() {
        return parent.climateSampler();
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, BiomeManager biomeManager, StructureFeatureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {

    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {

    }

    @Override
    public int getGenDepth() {
        return parent.getGenDepth();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {
        if (generateSealedWorld) {
            return parent.fillFromNoise(executor, blender, structureFeatureManager, chunk).whenCompleteAsync((chunkAccess, throwable) -> {
                BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(0,0,0);
                for (blockPos.setZ(0); blockPos.getZ() < 16; blockPos.setZ(blockPos.getZ() + 1)) {
                    for (blockPos.setX(0); blockPos.getX() < 16; blockPos.setX(blockPos.getX() + 1)) {
                        blockPos.setY(chunkAccess.getMaxBuildHeight());
                        while (blockPos.getY() > chunkAccess.getMinBuildHeight() && chunkAccess.getBlockState(blockPos).getBlock() instanceof AirBlock) {
                            blockPos.setY(blockPos.getY() - 1);
                        }
                        while (blockPos.getY() > chunkAccess.getMinBuildHeight()) {
                            chunkAccess.setBlockState(blockPos, Blocks.BEDROCK.defaultBlockState(), false);
                            blockPos.setY(blockPos.getY() - 1);
                        }
                        chunkAccess.setBlockState(blockPos, Blocks.VOID_AIR.defaultBlockState(), false);
                    }
                }
            });
        } else {
            return CompletableFuture.completedFuture(chunk);
        }
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return parent.getMinY();
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor heightAccessor) {
        return parent.getBaseHeight(x, z, heightMapType, heightAccessor);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor) {
        return parent.getBaseColumn(x, z, heightAccessor);
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor executor, Blender blender, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {
        return parent.createBiomes(biomes, executor, blender, structureFeatureManager, chunk);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int p_204416_, int p_204417_, int p_204418_) {
        return parent.getNoiseBiome(p_204416_, p_204417_, p_204418_);
    }

    @Override
    public Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> findNearestMapFeature(ServerLevel p_207971_, HolderSet<ConfiguredStructureFeature<?, ?>> p_207972_, BlockPos p_207973_, int p_207974_, boolean p_207975_) {
        return parent.findNearestMapFeature(p_207971_, p_207972_, p_207973_, p_207974_, p_207975_);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureFeatureManager structureFeatureManager) {

    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor heightAccessor) {
        return parent.getSpawnHeight(heightAccessor);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return parent.getBiomeSource();
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> p_204386_, StructureFeatureManager p_204387_, MobCategory p_204388_, BlockPos p_204389_) {
        return parent.getMobsAt(p_204386_, p_204387_, p_204388_, p_204389_);
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, StructureFeatureManager structureFeatureManager, ChunkAccess chunk, StructureManager structureManager, long seed) {

    }

    @Override
    public void createReferences(WorldGenLevel level, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {

    }

    @Override
    public int getFirstFreeHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor heightAccessor) {
        return parent.getFirstFreeHeight(x, z, heightMapType, heightAccessor);
    }

    @Override
    public int getFirstOccupiedHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor heightAccessor) {
        return parent.getFirstOccupiedHeight(x, z, heightMapType, heightAccessor);
    }

    @Override
    public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {

    }

    @Override
    public boolean hasFeatureChunkInRange(ResourceKey<StructureSet> p_212266_, long p_212267_, int p_212268_, int p_212269_, int p_212270_) {
        return parent.hasFeatureChunkInRange(p_212266_, p_212267_, p_212268_, p_212269_, p_212270_);
    }

    @Override
    public Stream<Holder<StructureSet>> possibleStructureSets() {
        return parent.possibleStructureSets();
    }

    @Override
    public void ensureStructuresGenerated() {
        parent.ensureStructuresGenerated();
    }
}
