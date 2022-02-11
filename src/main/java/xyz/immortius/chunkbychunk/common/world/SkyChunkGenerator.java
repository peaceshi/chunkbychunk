package xyz.immortius.chunkbychunk.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
     * @param parent              The chunkGenerator this generator is based on
     * @param generateSealedWorld Whether to generate a basic bedrock heightmap or not
     */
    public SkyChunkGenerator(ChunkGenerator parent, boolean generateSealedWorld) {
        super(parent.getBiomeSource(), parent.getSettings());
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

    public void createBiomes(Registry<Biome> p_62197_, ChunkAccess p_62198_) {
        parent.createBiomes(p_62197_, p_62198_);
    }

    public void applyCarvers(long p_62157_, BiomeManager p_62158_, ChunkAccess p_62159_, GenerationStep.Carving p_62160_) {
    }

    protected Aquifer createAquifer(ChunkAccess p_156162_) {
        return Aquifer.createDisabled(this.getSeaLevel(), Blocks.WATER.defaultBlockState());
    }

    public BlockPos findNearestMapFeature(ServerLevel p_62162_, StructureFeature<?> p_62163_, BlockPos p_62164_, int p_62165_, boolean p_62166_) {
        return parent.findNearestMapFeature(p_62162_, p_62163_, p_62164_, p_62165_, p_62166_);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion p_62168_, StructureFeatureManager p_62169_) {
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_62170_, ChunkAccess chunkAccess) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public StructureSettings getSettings() {
        return parent.getSettings();
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor p_156157_) {
        return parent.getSpawnHeight(p_156157_);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return parent.getBiomeSource();
    }

    @Override
    public int getGenDepth() {
        return parent.getGenDepth();
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome p_156158_, StructureFeatureManager p_156159_, MobCategory p_156160_, BlockPos p_156161_) {
        return parent.getMobsAt(p_156158_, p_156159_, p_156160_, p_156161_);
    }

    @Override
    public void createStructures(RegistryAccess p_62200_, StructureFeatureManager p_62201_, ChunkAccess p_62202_, StructureManager p_62203_, long p_62204_) {
    }

    @Override
    public void createReferences(WorldGenLevel p_62178_, StructureFeatureManager p_62179_, ChunkAccess p_62180_) {
    }

    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {
        if (generateSealedWorld) {
            return parent.fillFromNoise(executor, structureFeatureManager, chunk).whenCompleteAsync((chunkAccess, throwable) -> {
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
    public int getFirstFreeHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor heightAccessor) {
        return parent.getFirstFreeHeight(x, z, heightMapType, heightAccessor);
    }

    @Override
    public int getFirstOccupiedHeight(int x, int z, Heightmap.Types heightMapType, LevelHeightAccessor heightAccessor) {
        return parent.getFirstOccupiedHeight(x, z, heightMapType, heightAccessor);
    }

    @Override
    public boolean hasStronghold(ChunkPos chunkPos) {
        return parent.hasStronghold(chunkPos);
    }

    @Override
    public BaseStoneSource getBaseStoneSource() {
        return parent.getBaseStoneSource();
    }

}
