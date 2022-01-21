package xyz.immortius.chunkbychunk.common.blockEntities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import xyz.immortius.chunkbychunk.common.menus.WorldForgeMenu;
import xyz.immortius.chunkbychunk.interop.CBCInteropMethods;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkConstants;
import xyz.immortius.chunkbychunk.interop.ChunkByChunkSettings;

import java.util.Map;

/**
 * World Forge Block Entity - this holds the input and output of the world forge,
 * and handles dissolving the input to crystalise the output
 */
public class WorldForgeBlockEntity extends BaseFueledBlockEntity {
    public static final int NUM_ITEM_SLOTS = 2;
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_RESULT = 1;

    public static final int NUM_DATA_ITEMS = 2;
    public static final int DATA_PROGRESS = 0;
    public static final int DATA_GOAL = 1;

    private static final int GROW_CRYSTAL_AT = 4;
    private static final Map<Item, FuelValueSupplier> FUEL;
    private static final Map<Item, FuelValueSupplier> CRYSTAL_COSTS;
    private static final Item INITIAL_CRYSTAL = ChunkByChunkConstants.worldFragmentItem();
    private static final Map<Item, Item> CRYSTAL_STEPS;

    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT};

    private int progress;
    private int goal;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int id) {
            switch (id) {
                case 0:
                    return progress;
                case 1:
                    return goal;
                default:
                    return 0;
            }
        }

        public void set(int id, int value) {
            switch (id) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    goal = value;
                    break;
            }

        }

        public int getCount() {
            return NUM_DATA_ITEMS;
        }
    };

    static {
        ImmutableMap.Builder<Item, FuelValueSupplier> fuelBuilder = ImmutableMap.builder();
        for (Item value : CBCInteropMethods.getTaggedItems("chunkbychunk:weakworldforgefuel")) {
            fuelBuilder.put(value, ChunkByChunkSettings::worldForgeSoilFuelValue);
        }
        for (Item value : CBCInteropMethods.getTaggedItems("chunkbychunk:worldforgefuel")) {
            fuelBuilder.put(value, ChunkByChunkSettings::worldForgeStoneFuelValue);
        }

        fuelBuilder.put(ChunkByChunkConstants.worldFragmentItem(), ChunkByChunkSettings::worldForgeFuelPerFragment);
        fuelBuilder.put(ChunkByChunkConstants.worldShardItem(), () -> ChunkByChunkSettings.worldForgeFuelPerFragment() * 4);
        fuelBuilder.put(ChunkByChunkConstants.worldCrystalItem(), () -> ChunkByChunkSettings.worldForgeFuelPerFragment() * 16);

        FUEL = fuelBuilder.build();

        CRYSTAL_COSTS = ImmutableMap.<Item, FuelValueSupplier>builder()
                .put(ChunkByChunkConstants.worldFragmentItem(), ChunkByChunkSettings::worldForgeFuelPerFragment)
                .put(ChunkByChunkConstants.worldShardItem(), () -> ChunkByChunkSettings.worldForgeFuelPerFragment() * 4)
                .put(ChunkByChunkConstants.worldCrystalItem(), () -> ChunkByChunkSettings.worldForgeFuelPerFragment() * 16)
                .put(ChunkByChunkConstants.worldCoreBlockItem(), () -> ChunkByChunkSettings.worldForgeFuelPerFragment() * 64).build();

        CRYSTAL_STEPS = ImmutableMap.<Item, Item>builder()
                .put(ChunkByChunkConstants.worldFragmentItem(), ChunkByChunkConstants.worldShardItem())
                .put(ChunkByChunkConstants.worldShardItem(), ChunkByChunkConstants.worldCrystalItem())
                .put(ChunkByChunkConstants.worldCrystalItem(), ChunkByChunkConstants.worldCrystalItem()).build();
    }

    public WorldForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ChunkByChunkConstants.worldForgeEntity(), pos, state, NUM_ITEM_SLOTS, SLOT_INPUT, FUEL);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.chunkbychunk.worldforge");
    }

    @Override
    protected AbstractContainerMenu createMenu(int menuId, Inventory inventory) {
        return new WorldForgeMenu(menuId, inventory, this, this.dataAccess);
    }

    public static boolean isWorldForgeFuel(ItemStack itemStack) {
        return FUEL.getOrDefault(itemStack.getItem(), () -> 0).get() > 0;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("Progress");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag result = super.save(tag);
        result.putInt("Progress", this.progress);
        return result;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, WorldForgeBlockEntity entity) {
        // Consume available fuel
        if (entity.getRemainingFuel() > 0) {
            int consumeAmount = entity.consumeFuel(ChunkByChunkSettings.worldForgeProductionRate());
            entity.progress += consumeAmount;
        }

        // Determine what crystal we're making and its cost
        ItemStack outputItems = entity.getItem(SLOT_RESULT);
        Item producingItem;
        if (outputItems.isEmpty()) {
            producingItem = INITIAL_CRYSTAL;
        } else {
            producingItem = outputItems.getItem();
        }

        // Check if we've reached the finished result or otherwise blocked
        if (outputItems.getCount() == outputItems.getMaxStackSize()) {
            return;
        }

        boolean changed = entity.checkConsumeFuelItem();

        int itemCost = CRYSTAL_COSTS.get(producingItem).get();
        Item nextItem = CRYSTAL_STEPS.get(producingItem);
        entity.goal = itemCost;

        // Produce the item if we can, replacing the existing item
        if (entity.progress >= itemCost) {
            entity.progress -= itemCost;
            changed = true;
            if (outputItems.isEmpty()) {
                entity.setItem(SLOT_RESULT, producingItem.getDefaultInstance());
            } else if (outputItems.getCount() == GROW_CRYSTAL_AT - 1 && nextItem != null) {
                entity.setItem(SLOT_RESULT, nextItem.getDefaultInstance());
                entity.goal = CRYSTAL_COSTS.get(nextItem).get();
            } else {
                outputItems.grow(1);
            }
        }

        if (changed) {
            setChanged(level, blockPos, blockState);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return SLOTS_FOR_UP;
        }
        return SLOTS_FOR_DOWN;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot == SLOT_RESULT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack item) {
        if (slot == SLOT_RESULT) {
            return false;
        }
        return super.canPlaceItem(slot, item);
    }

}
