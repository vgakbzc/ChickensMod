package com.setycz.chickens.block.breeder;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.block.BlockInit;
import com.setycz.chickens.block.accelerators.BlockAccelerator;
import com.setycz.chickens.block.packed_chicken.BlockPackedChicken;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBreeder extends TileEntity implements ITickable {

    private int tickInterval = 0;
    private final int MIN_TICK_INTERVAL = 60;

    public void update() {
        if(tickInterval < MIN_TICK_INTERVAL) {
            ++tickInterval;
            return;
        }
        tickInterval = 0;
        List<ChickensRegistryItem> chickens = getNeighborChickens();
        int size = chickens.size();
        if(size < 2) {
            return;
        }
        int i1 = ChickensMod.RANDOM.nextInt(size);
        int i2 = ChickensMod.RANDOM.nextInt(size - 1);
        if(i2 >= i1) {
            ++i2;
        }
        float difficulty = (float) Math.sqrt(chickens.get(i1).getRarity() * chickens.get(i2).getRarity()) * 10f;
        difficulty /= Math.max(calculateNeighborBonus(), 1.0f);
        if(ChickensMod.RANDOM.nextInt(65536) * difficulty < Math.ceil(65536)) {
            Block block = BlockInit.PackedChickens.getBlockByChicken(ChickensRegistry.getRandomChild(chickens.get(i1), chickens.get(i2)));
            this.getWorld().removeTileEntity(getPos());
            this.getWorld().setBlockState(getPos(), block.getDefaultState().withProperty(BlockPackedChicken.FACING, getAvailableFacing()));
        }
    }

    private float calculateNeighborBonus() {
        BlockPos blockPos = this.getPos();
        float countAccelerator = 0;
        for(EnumFacing facing : EnumFacing.values()) {
            BlockPos neighborBlockPos = blockPos.offset(facing);
            IBlockState blockstate = this.getWorld().getBlockState(neighborBlockPos);
            if(blockstate.getBlock() instanceof BlockAccelerator) {
                countAccelerator += ((BlockAccelerator) blockstate.getBlock()).getAccelerationMultiplier();
            }
        }
        return 1.0f + (float) Math.pow(countAccelerator + 1, 1.7);
    }

    private List<ChickensRegistryItem> getNeighborChickens() {
        BlockPos blockPos = this.getPos();
        List<ChickensRegistryItem> chickens = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos neighborBlockPos = blockPos.offset(facing);
            IBlockState blockstate = this.getWorld().getBlockState(neighborBlockPos);
            if(blockstate.getBlock() instanceof BlockPackedChicken) {
                chickens.add(((BlockPackedChicken) blockstate.getBlock()).getChicken());
            }
        }
        return chickens;
    }

    private EnumFacing getAvailableFacing() {
        BlockPos blockPos = this.getPos();
        List<ChickensRegistryItem> chickens = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos neighborBlockPos = blockPos.offset(facing);
            IBlockState blockstate = this.getWorld().getBlockState(neighborBlockPos);
            if(blockstate.getBlock().equals(Blocks.AIR)) {
                return facing;
            }
        }
        return EnumFacing.NORTH;
    }

}
