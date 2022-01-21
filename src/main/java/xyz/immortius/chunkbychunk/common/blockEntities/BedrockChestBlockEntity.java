package xyz.immortius.chunkbychunk.common.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xyz.immortius.chunkbychunk.common.menus.BedrockChestMenu;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkConstants;

/**
 * Entity for the Bedrock Chest Block. It is a chest with a single item slot.
 */
public class BedrockChestBlockEntity extends RandomizableContainerBlockEntity {

    public static final int COLUMNS = 1;
    public static final int ROWS = 1;
    public static final int CONTAINER_SIZE = COLUMNS * ROWS;

    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public BedrockChestBlockEntity(BlockPos pos, BlockState state) {
        super(ChunkByChunkConstants.bedrockChestEntity(), pos, state);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.chunkbychunk.bedrockchest");
    }

    @Override
    protected AbstractContainerMenu createMenu(int menuId, Inventory inventory) {
        return new BedrockChestMenu(menuId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public void load(CompoundTag p_155678_) {
        super.load(p_155678_);
        this.loadFromTag(p_155678_);
    }

    @Override
    public CompoundTag save(CompoundTag p_59676_) {
        super.save(p_59676_);
        return this.saveToTag(p_59676_);
    }

    public void loadFromTag(CompoundTag tag) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag) && tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    public CompoundTag saveToTag(CompoundTag tag) {
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, false);
        }
        return tag;
    }
    
}
