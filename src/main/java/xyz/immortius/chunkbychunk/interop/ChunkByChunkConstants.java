package xyz.immortius.chunkbychunk.interop;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.immortius.chunkbychunk.common.blockEntities.BedrockChestBlockEntity;
import xyz.immortius.chunkbychunk.common.blockEntities.WorldForgeBlockEntity;
import xyz.immortius.chunkbychunk.common.blockEntities.WorldScannerBlockEntity;
import xyz.immortius.chunkbychunk.common.menus.BedrockChestMenu;
import xyz.immortius.chunkbychunk.common.menus.WorldForgeMenu;
import xyz.immortius.chunkbychunk.common.menus.WorldScannerMenu;
import xyz.immortius.chunkbychunk.fabric.ChunkByChunkMod;

/**
 * Constants for ChunkByChunk - may vary by mod system
 */
public final class ChunkByChunkConstants {

    private ChunkByChunkConstants() {
    }

    public static final Logger LOGGER = LogManager.getLogger(ChunkByChunkConstants.MOD_ID);

    public static final String MOD_ID = "chunkbychunk";

    public static final String DEFAULT_CONFIG_PATH = "defaultconfigs";
    public static final String CONFIG_FILE = MOD_ID + ".toml";

    public static final ResourceKey<Level> SKY_CHUNK_GENERATION_LEVEL = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MOD_ID, "skychunkgeneration"));

    /// Blocks

    public static Block spawnChunkBlock() {
        return ChunkByChunkMod.SPAWN_CHUNK_BLOCK;
    }

    public static Block unstableSpawnChunkBlock() {
        return ChunkByChunkMod.UNSTABLE_SPAWN_CHUNK_BLOCK;
    }

    public static Block bedrockChestBlock() {
        return ChunkByChunkMod.BEDROCK_CHEST_BLOCK;
    }

    public static Block worldCoreBlock() { return ChunkByChunkMod.WORLD_CORE_BLOCK; }

    public static Block worldForgeBlock() { return ChunkByChunkMod.WORLD_FORGE_BLOCK; }

    public static Block worldScannerBlock() { return ChunkByChunkMod.WORLD_SCANNER_BLOCK; }

    // Block Items

    public static Item spawnChunkBlockItem() {
        return ChunkByChunkMod.SPAWN_CHUNK_BLOCK_ITEM;
    }

    public static Item unstableChunkSpawnBlockItem() {
        return ChunkByChunkMod.UNSTABLE_SPAWN_CHUNK_BLOCK_ITEM;
    }

    public static Item bedrockChestItem() {
        return ChunkByChunkMod.BEDROCK_CHEST_BLOCK_ITEM;
    }

    public static Item worldCoreBlockItem() { return ChunkByChunkMod.WORLD_CORE_BLOCK_ITEM; }

    public static Item worldForgeBlockItem() { return ChunkByChunkMod.WORLD_FORGE_BLOCK_ITEM; }

    public static Item worldScannerBlockItem() { return ChunkByChunkMod.WORLD_SCANNER_BLOCK_ITEM; }

    // Items

    public static Item worldFragmentItem() { return ChunkByChunkMod.WORLD_FRAGMENT_ITEM; }

    public static Item worldShardItem() { return ChunkByChunkMod.WORLD_SHARD_ITEM; }

    public static Item worldCrystalItem() { return ChunkByChunkMod.WORLD_CRYSTAL_ITEM; }

    // Block Entities

    public static BlockEntityType<BedrockChestBlockEntity> bedrockChestEntity() {
        return ChunkByChunkMod.BEDROCK_CHEST_BLOCK_ENTITY;
    }

    public static BlockEntityType<WorldForgeBlockEntity> worldForgeEntity() {
        return ChunkByChunkMod.WORLD_FORGE_BLOCK_ENTITY;
    }

    public static BlockEntityType<WorldScannerBlockEntity> worldScannerEntity() {
        return ChunkByChunkMod.WORLD_SCANNER_BLOCK_ENTITY;
    }

    // Sound Events

    public static SoundEvent spawnChunkSoundEffect() {
        return ChunkByChunkMod.SPAWN_CHUNK_SOUND_EVENT;
    }

    // Menus

    public static MenuType<BedrockChestMenu> bedrockChestMenu() {
        return ChunkByChunkMod.BEDROCK_CHEST_MENU;
    }

    public static MenuType<WorldForgeMenu> worldForgeMenu() {
        return ChunkByChunkMod.WORLD_FORGE_MENU;
    }

    public static MenuType<WorldScannerMenu> worldScannerMenu() {
        return ChunkByChunkMod.WORLD_SCANNER_MENU;
    }
}
