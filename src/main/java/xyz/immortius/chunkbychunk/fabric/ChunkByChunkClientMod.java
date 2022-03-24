package xyz.immortius.chunkbychunk.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.immortius.chunkbychunk.client.screens.BedrockChestScreen;
import xyz.immortius.chunkbychunk.client.screens.WorldForgeScreen;
import xyz.immortius.chunkbychunk.client.screens.WorldScannerScreen;
import xyz.immortius.chunkbychunk.common.world.SpawnChunkHelper;
import xyz.immortius.chunkbychunk.config.ChunkByChunkConfig;
import xyz.immortius.chunkbychunk.fabric.mixins.WorldPresetAccessor;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkConstants;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkSettings;

/**
 * Client-only mod initialization
 */
public class ChunkByChunkClientMod implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ChunkByChunkConstants.MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Client Initializing");
        ScreenRegistry.register(ChunkByChunkConstants.bedrockChestMenu(), BedrockChestScreen::new);
        ScreenRegistry.register(ChunkByChunkConstants.worldForgeMenu(), WorldForgeScreen::new);
        ScreenRegistry.register(ChunkByChunkConstants.worldScannerMenu(), WorldScannerScreen::new);

        WorldPresetAccessor.getValues().add(new SkyChunkWorldPreset(ChunkByChunkConstants.MOD_ID + ".onechunkskyworld", false));
        WorldPresetAccessor.getValues().add(new SkyChunkWorldPreset(ChunkByChunkConstants.MOD_ID + ".onechunksealedworld", true));

        ClientPlayNetworking.registerGlobalReceiver(ChunkByChunkMod.CONFIG_PACKET, (client, handler, buf, responseSender) -> {
            LOGGER.info("Receiving config from server");
            ChunkByChunkConfig.get().getGameplayConfig().setBlockPlacementAllowedOutsideSpawnedChunks(buf.readBoolean());
        });
    }

}
