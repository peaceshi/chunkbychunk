package xyz.immortius.chunkbychunk.forge;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import xyz.immortius.chunkbychunk.client.screens.ChunkByChunkConfigScreen;

public final class ChunkByChunkClientMod {

    private ChunkByChunkClientMod() {
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ChunkByChunkConfigScreen(screen)));
    }
}
