package xyz.immortius.chunkbychunk.interop;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelResource;
import xyz.immortius.chunkbychunk.config.ChunkByChunkConfig;
import xyz.immortius.chunkbychunk.config.system.ConfigSystem;
import xyz.immortius.chunkbychunk.fabric.ChangeDimensionImpl;
import xyz.immortius.chunkbychunk.fabric.mixins.BucketFluidAccessor;



/**
 * Static methods whose implementation varies by mod system
 */
public final class CBCInteropMethods {
    public static final String SERVERCONFIG = "serverconfig";
    private static final ConfigSystem configSystem = new ConfigSystem();

    private CBCInteropMethods() {

    }

    /**
     * Change the dimension of an entity.
     * For fabric, we use a mixin to enable dimension changes
     *
     * @param entity     The entity to move
     * @param level      The level to move the entity to
     * @param portalInfo Portal information for the move
     * @return The moved entity
     */
    public static Entity changeDimension(Entity entity, ServerLevel level, PortalInfo portalInfo) {
        return ChangeDimensionImpl.changeDimension(entity, level, portalInfo);
    }

    /**
     * @param bucket The bucket to get the contents of
     * @return The fluid contents of the bucket (may be null)
     */
    public static Fluid getBucketContents(BucketItem bucket) {
        return ((BucketFluidAccessor) bucket).getFluid();
    }

    /**
     * Loads configuration for a server.
     *
     * @param server The server to load config for
     */
    public static void loadServerConfig(MinecraftServer server) {
        configSystem.synchConfig(server.getWorldPath(LevelResource.ROOT).resolve(SERVERCONFIG).resolve(ChunkByChunkConstants.MOD_ID + ".toml"), ChunkByChunkConfig.get());
    }
}
