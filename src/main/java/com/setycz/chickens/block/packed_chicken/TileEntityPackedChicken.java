package com.setycz.chickens.block.packed_chicken;

import com.setycz.chickens.capabilities.InventoryStorageModifiable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class TileEntityPackedChicken extends TileEntity {

    private Item layItem = null;
    private float currentProgress = 0;
    private float maxProgress = 1.0f;

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

    public void progress(float multiplier) {
        currentProgress += multiplier;
        if(currentProgress > maxProgress) {

            int count = (int) Math.floor(Math.min(2E9, currentProgress / maxProgress));
            ItemStack layItemStack = new ItemStack(this.layItem, count);
            pushItemStack(layItemStack);

            currentProgress = Math.max(0f, currentProgress - count * maxProgress);

        }
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
        compound.setTag("LayItem", (new ItemStack(this.layItem)).serializeNBT());
        compound.setFloat("MaxProgress", this.maxProgress);
        compound.setFloat("CurrentProgress", this.currentProgress);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        int count = compound.getInteger("ItemCount");
        this.layItem = (new ItemStack(compound.getCompoundTag("LayItem"))).getItem();
        pushItemStack(new ItemStack(this.layItem, count));
        this.maxProgress = compound.getFloat("MaxProgress");
        this.currentProgress = compound.getFloat("CurrentProgress");
    }

    public void setMaxProgress(float maxProgress) {
        if(maxProgress <= 1.0f) {
            this.maxProgress = 1.0f;
        } else {
            this.maxProgress = maxProgress;
        }
    }
    public void setLayItem(Item layItem) {
        this.layItem = layItem;
    }

}
