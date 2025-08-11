package com.setycz.chickens.block.packed_chicken;

import com.setycz.chickens.block.accelerators.BlockAccelerator;
import com.setycz.chickens.capabilities.InventoryStorageModifiable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class TileEntityPackedChicken extends TileEntity implements ITickable {

    private float currentProgress = 0;
    private float multiplier = 1.0f;
    private int tickInterval = 0;
    private final int MIN_TICK_INTERVAL = 20;
    private ItemStack layItem;
    private float maxProgress = 1.0f;
    private boolean isInit = false;

    private final InventoryStorageModifiable slots = new InventoryStorageModifiable("container.packed_chicken", 1)
    {
        @Override public boolean canInsertSlot(int index, ItemStack stackIn) {
            return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 32768;
        }

        @Override
        public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            this.validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }
            }
            if (!simulate) {
                if (existing.isEmpty()) {
                    this.stacks.set(slot, stack);
                } else {
                    existing.grow(stack.getCount());
                }

                this.onContentsChanged(slot);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
            if (amount == 0) {
                return ItemStack.EMPTY;
            }
            this.validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (existing.getCount() <= amount) {
                if (!simulate) {
                    this.stacks.set(slot, ItemStack.EMPTY);
                    this.onContentsChanged(slot);
                }

                return existing;
            }
            if (!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - amount));
                this.onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, amount);
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if(canInsertSlot(slot, stack))
                return insertItemInternal(slot, stack, simulate);
            else
                return stack;
        }

        @Override
        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if(canExtractSlot(slot))
                return extractItemInternal(slot, amount, simulate);

            return ItemStack.EMPTY;
        }
    };

    public TileEntityPackedChicken() {
        super();
    }

    public void dropContents() {
        for (int i = 0; i < this.slots.getSlots(); ++i) {
            ItemStack stack = this.slots.extractItemInternal(i, this.slots.getSlotLimit(i), false);
            if(!stack.isEmpty()) {
                this.world.spawnEntity(new EntityItem(world, this.pos.getX(), this.pos.getY()+1, this.pos.getZ(), stack));
            }
        }
    }

    public ItemStack pushItemStack(ItemStack stack) {
        stack = slots.insertItemInternal(0, stack, false);
        markDirty();
        return stack;
    }

    public void update() {
        if(!isInit) {
            try{ init(); } catch(NullPointerException exception){
                // isInit = false;
                return;
            }
            isInit = true;
        }
        if(tickInterval < MIN_TICK_INTERVAL) {
            ++tickInterval; return;
        }
        calculateNeighborBonus();
        currentProgress += multiplier * tickInterval;
        tickInterval = 0;
        if(currentProgress > maxProgress) {

            int count = (int) Math.floor(Math.min(2E9, currentProgress / maxProgress));
            pushItemStack(getLayItem(count));

            currentProgress = Math.max(0f, currentProgress - count * maxProgress);

        }
    }

    private void calculateNeighborBonus() {
        BlockPos blockPos = this.getPos();
        int countSpecie = 0; float countAccelerator = 0;
        final float specieSpeedBonus[] = {1.0f, 1.5f, 3.0f, 6.0f, 10.0f, 20.0f, 60.0f, 100.0f};
        for(EnumFacing facing : EnumFacing.values()) {
            BlockPos neighborBlockPos = blockPos.offset(facing);
            IBlockState blockstate = this.getWorld().getBlockState(neighborBlockPos);
            if(blockstate.getBlock() == this.getBlockType()) {
                countSpecie++;
            }
            if(blockstate.getBlock() instanceof BlockAccelerator) {
                countAccelerator += ((BlockAccelerator) blockstate.getBlock()).getAccelerationMultiplier();
            }
        }
        this.multiplier = specieSpeedBonus[countSpecie] * (1.0f + countAccelerator);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.slots;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ItemCount", slots.getStackInSlot(0).getCount());
        compound.setFloat("CurrentProgress", this.currentProgress);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        init();
        int count = compound.getInteger("ItemCount");
        pushItemStack(this.getLayItem(count));
        this.currentProgress = compound.getFloat("CurrentProgress");
    }
    public float getEtaTime() { return (maxProgress - currentProgress) / multiplier; }
    public float getMaxTime() { return maxProgress / multiplier; }
    public float getEfficiency() { return 1.0f / getMaxTime(); }
    public float getEfficiencyMultiplier() { return multiplier; }

    private void init() {
        Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
        if(block instanceof BlockPackedChicken) {
            float maxProgress = 0.25f * ((BlockPackedChicken) block).getChicken().getMinLayTime();
            if(maxProgress < 1.0f) {
                this.maxProgress = 1.0f;
            } else {
                this.maxProgress = maxProgress;
            }
            this.layItem = ((BlockPackedChicken) block).getChicken().getLayItemHolder().getStack();
        }
    }
    private ItemStack getLayItem(int count) {
        ItemStack stack = this.layItem.copy();
        stack.setCount(count);
        return stack;
    }

}
