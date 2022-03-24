package xyz.immortius.chunkbychunk.fabric.mixins;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * Mixin to support accessing and adding to the list of WorldPresets
 */
@Mixin(WorldPreset.class)
public interface WorldPresetAccessor {
    @Accessor("PRESETS")
    public static List<WorldPreset> getValues() {
        throw new AssertionError();
    }
}
