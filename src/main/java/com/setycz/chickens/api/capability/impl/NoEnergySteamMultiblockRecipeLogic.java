package com.setycz.chickens.api.capability.impl;

import gregtech.api.capability.impl.SteamMultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.metatileentity.multiblock.ParallelLogicType;
import gregtech.api.recipes.RecipeBuilder;
import org.jetbrains.annotations.NotNull;
import gregtech.api.GTValues;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.common.ConfigHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NoEnergySteamMultiblockRecipeLogic extends SteamMultiblockRecipeLogic {

    public NoEnergySteamMultiblockRecipeLogic(RecipeMapSteamMultiblockController tileEntity, int parallel) {
        super(tileEntity, tileEntity.recipeMap, tileEntity.getSteamFluidTank(), 1.0);
        setParallelLimit(parallel);
    }

    public @NotNull ParallelLogicType getParallelLogicType() {
        return ParallelLogicType.APPEND_ITEMS;
    }

    @Override protected long getEnergyStored() {
        return (long) Integer.MAX_VALUE;
    }

    @Override protected long getEnergyCapacity() {
        return (long) Integer.MAX_VALUE;
    }

    @Override protected boolean drawEnergy(int recipeEUt, boolean simulate) {
        return true;
    }

    @Override protected boolean hasEnoughPower(int[] resultOverclock) {
        return true;
    }

}
