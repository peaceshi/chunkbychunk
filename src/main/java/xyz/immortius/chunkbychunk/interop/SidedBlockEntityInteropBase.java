package xyz.immortius.chunkbychunk.interop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class for SidedBlocks with top/side/bottom item handler capabilities. In forge, this changes the ITEM_HANDLER_CAPABILITY.
 */
public abstract class SidedBlockEntityInteropBase extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
    protected SidedBlockEntityInteropBase(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
        super(p_155076_, p_155077_, p_155078_);
    }
}
