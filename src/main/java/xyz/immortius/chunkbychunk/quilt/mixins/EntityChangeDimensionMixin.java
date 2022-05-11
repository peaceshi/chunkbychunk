package xyz.immortius.chunkbychunk.quilt.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.immortius.chunkbychunk.quilt.ChangeDimensionImpl;

/**
 * A mixin to enable the ability to change the dimension of entities without using a portal (and to custom dimensions)
 */
@Mixin(Entity.class)
public abstract class EntityChangeDimensionMixin {

    @Inject(method = "findDimensionEntryPoint", at = @At("RETURN"), cancellable = true)
    private void changeReturnValue(CallbackInfoReturnable<PortalInfo> cir) {
        if (cir.getReturnValue() == null && ChangeDimensionImpl.getPortalInfo() != null) {
            cir.setReturnValue(ChangeDimensionImpl.getPortalInfo());
        }
    }
}
