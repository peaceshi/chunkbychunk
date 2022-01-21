package xyz.immortius.chunkbychunk.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The Base Sky Chunk Generator - Sky Chunk Generators wrap a parent generator but disable actual generation. The
 * parent generator is retained for biome information and similar. Each sky chunk generator also has a reference
 * to the dimension that generates chunks for this dimension
 */
public abstract class BaseSkyChunkGenerator extends ChunkGenerator {

    protected final ChunkGenerator parent;
    protected final ResourceKey<Level> generationLevel;

    /**
     * @param parent The chunkGenerator this generator is based on
     */
    public BaseSkyChunkGenerator(ChunkGenerator parent, ResourceKey<Level> generationLevel) {
        super(parent.getBiomeSource(), parent.getSettings());
        this.parent = parent;
        this.generationLevel = generationLevel;
    }

    /**
     * @param parent The chunkGenerator this generator is based on
     * @param structureSettings Structure settings to use, if not from the parent generator
     */
    public BaseSkyChunkGenerator(ChunkGenerator parent, ResourceKey<Level> generationLevel, StructureSettings structureSettings) {
        super(parent.getBiomeSource(), structureSettings);
        this.parent = parent;
        this.generationLevel = generationLevel;
    }

    public ChunkGenerator getParent() {
        return parent;
    }

    public ResourceKey<Level> getGenerationLevel() {
        return generationLevel;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_156171_, StructureFeatureManager p_156172_, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public void createBiomes(Registry<Biome> biomeRegistry, ChunkAccess chunkAccess) {
        parent.createBiomes(biomeRegistry, chunkAccess);
    }

    @Override
    public void applyCarvers(long p_62157_, BiomeManager p_62158_, ChunkAccess p_62159_, GenerationStep.Carving p_62160_) {
    }

    @Override
    protected Aquifer createAquifer(ChunkAccess p_156162_) {
        return Aquifer.createDisabled(this.getSeaLevel(), Blocks.WATER.defaultBlockState());
    }

    @Override
    public BlockPos findNearestMapFeature(ServerLevel level, StructureFeature<?> feature, BlockPos pos, int p_62165_, boolean p_62166_) {
        return parent.findNearestMapFeature(level, feature, pos, p_62165_, p_62166_);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion p_62168_, StructureFeatureManager p_62169_) {
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_62170_, ChunkAccess p_62171_) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public StructureSettings getSettings() {
        return parent.getSettings();
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
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Biome biome, StructureFeatureManager structureFeatureManager, MobCategory mobCategory, BlockPos pos) {
        return parent.getMobsAt(biome, structureFeatureManager, mobCategory, pos);
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, StructureFeatureManager structureFeatureManager, ChunkAccess chunk, StructureManager structureManager, long seed) {
    }

    @Override
    public void createReferences(WorldGenLevel level, StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {
    }

    @Override
    public int getFirstFreeHeight(int p_156175_, int p_156176_, Heightmap.Types p_156177_, LevelHeightAccessor p_156178_) {
        return parent.getFirstFreeHeight(p_156175_, p_156176_, p_156177_, p_156178_);
    }

    @Override
    public int getGenDepth() {
        return parent.getGenDepth();
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
    public BaseStoneSource getBaseStoneSource() {
        return parent.getBaseStoneSource();
    }
}
