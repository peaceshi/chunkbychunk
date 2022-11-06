package xyz.immortius.chunkbychunk.common.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import xyz.immortius.chunkbychunk.common.world.SkyChunkGenerator;

/**
 * Spawns a chunk from the equivalent chunk in the source dimension (with configuration offset)
 */
public class SpawnChunkBlock extends BaseSpawnChunkBlock {

    public SpawnChunkBlock(Block triggeredSpawnChunkBlock, Properties blockProperties) {
        super(triggeredSpawnChunkBlock.defaultBlockState(), blockProperties);
    }

    @Override
    public boolean isValidForLevel(ServerLevel level) {
        return level.getChunkSource().getGenerator() instanceof SkyChunkGenerator generator && generator.isChunkSpawnerAllowed();
    }
}
