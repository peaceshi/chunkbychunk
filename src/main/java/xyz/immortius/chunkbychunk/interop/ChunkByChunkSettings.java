package xyz.immortius.chunkbychunk.interop;

import xyz.immortius.chunkbychunk.config.ChunkRewardChestContent;
import xyz.immortius.chunkbychunk.config.ChunkByChunkConfig;

/**
 * Access to ChunkByChunk settings
 */
public class ChunkByChunkSettings {

    public static boolean sealWorld() { return ChunkByChunkConfig.get().getGeneration().sealWorld(); }

    public static boolean spawnNewChunkChest() {
        return ChunkByChunkConfig.get().getGeneration().spawnNewChunkChest();
    }

    public static boolean useBedrockChest() { return ChunkByChunkConfig.get().getGeneration().useBedrockChest(); }

    public static int minChestSpawnDepth() {
        return ChunkByChunkConfig.get().getGeneration().getMinChestSpawnDepth();
    }

    public static int maxChestSpawnDepth() {
        return ChunkByChunkConfig.get().getGeneration().getMaxChestSpawnDepth();
    }

    public static int initialChunks() {
        return ChunkByChunkConfig.get().getGeneration().getInitialChunks();
    }

    public static int chestQuantity() {
        return ChunkByChunkConfig.get().getGeneration().getChestQuantity();
    }

    public static ChunkRewardChestContent chestContents() {
        return ChunkByChunkConfig.get().getGeneration().getChestContents();
    }

    public static int bedrockChestBlocksRemainingThreshold() {
        return ChunkByChunkConfig.get().getBedrockChest().getBedrockChestBlocksRemainingThreshold();
    }

    public static int chunkGenXOffset() {
        return ChunkByChunkConfig.get().getGeneration().getChunkGenXOffset();
    }

    public static int chunkGenZOffset() {
        return ChunkByChunkConfig.get().getGeneration().getChunkGenZOffset();
    }

    public static int worldForgeProductionRate() {
        return ChunkByChunkConfig.get().getWorldForge().getProductionRate();
    }

    public static int worldForgeSoilFuelValue() {
        return ChunkByChunkConfig.get().getWorldForge().getSoilFuelValue();
    }

    public static int worldForgeStoneFuelValue() {
        return ChunkByChunkConfig.get().getWorldForge().getStoneFuelValue();
    }

    public static int worldForgeFuelPerFragment() {
        return ChunkByChunkConfig.get().getWorldForge().getFragmentFuelCost();
    }

    public static boolean isBlockPlacementAllowedOutsideSpawnedChunks() {
        return ChunkByChunkConfig.get().getGameplayConfig().isBlockPlacementAllowedOutsideSpawnedChunks();
    }

    public static int worldScannerFuelPerFragment() {
        return ChunkByChunkConfig.get().getWorldScannerConfig().getFuelPerFragment();
    }

    public static int worldScannerFuelConsumedPerTick() {
        return ChunkByChunkConfig.get().getWorldScannerConfig().getFuelConsumedPerTick();
    }

    public static int worldScannerFuelRequiredPerChunk() {
        return ChunkByChunkConfig.get().getWorldScannerConfig().getFuelRequiredPerChunk();
    }
}
