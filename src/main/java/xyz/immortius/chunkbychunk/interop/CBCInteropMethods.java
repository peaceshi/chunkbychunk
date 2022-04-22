package xyz.immortius.chunkbychunk.interop;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.PortalInfo;
import xyz.immortius.chunkbychunk.quilt.ChangeDimensionImpl;
import xyz.immortius.chunkbychunk.quilt.mixins.BucketFluidAccessor;

/**
 * Static methods whose implementation varies by mod system
 */
public final class CBCInteropMethods {

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

}
