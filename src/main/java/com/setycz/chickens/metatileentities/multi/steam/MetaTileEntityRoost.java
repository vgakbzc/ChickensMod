package com.setycz.chickens.metatileentities.multi.steam;

import com.setycz.chickens.api.capability.impl.NoEnergySteamMultiblockRecipeLogic;
import com.setycz.chickens.api.recipe.ChickensRecipeMaps;
import org.jetbrains.annotations.NotNull;
import gregtech.api.capability.impl.SteamMultiWorkable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import magicbook.gtlitecore.api.capability.impl.NoEnergyMultiblockRecipeLogic;
import magicbook.gtlitecore.api.metatileentity.multi.NoEnergyMultiblockController;
import net.minecraft.util.EnumFacing;
import gregtech.api.pattern.TraceabilityPredicate;
import net.minecraft.util.Rotation;
import gregtech.api.metatileentity.multiblock.ParallelLogicType;

import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityRoost extends RecipeMapSteamMultiblockController {

    private static final int PARALLEL_LIMIT = 16;

    public MetaTileEntityRoost(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, ChickensRecipeMaps.ROOST_RECIPES, 1.0);
        this.recipeMapWorkable = new RoostRecipeLogic(this, PARALLEL_LIMIT);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityRoost(metaTileEntityId);
    }

    @NotNull @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle(" AAA ", " CCC ", " CCC ", " CCC ", " AAA ")
            .aisle("ADDDA", "C   C", "C   C", "C   C", "AAAAA")
            .aisle("ADDDA", "C   C", "C   C", "C   C", "AAEAA")
            .aisle("ADDDA", "C   C", "C   C", "C   C", "AAAAA")
            .aisle(" ASA ", " CCC ", " CCC ", " CCC ", " AAA ")
            .where('S', this.selfPredicate())
            .where('A', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.COKE_BRICKS))
                .setMinGlobalLimited(9)
                .or(autoAbilities(false, false, true, true, false)))
            .where('C', getGlassStates())
            .where('D', states(Blocks.HAY_BLOCK.getDefaultState()))
            .where('E', states(Blocks.GLOWSTONE.getDefaultState()))
            .build();
    }
    private TraceabilityPredicate getGlassStates() {
        TraceabilityPredicate state = states(Blocks.GLASS.getDefaultState());
        for(int i = 0; i < 16; ++i ) {
            state = state.or(states(Blocks.STAINED_GLASS.getStateFromMeta(i)));
        }
        return state;
    }

    @SideOnly(Side.CLIENT) @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.COKE_BRICKS;
    }

    @SideOnly(Side.CLIENT) @Override @NotNull
    protected ICubeRenderer getFrontOverlay() { return Textures.VACUUM_FREEZER_OVERLAY; }

    @Override
    public boolean hasMaintenanceMechanics() { return false; }

    public class RoostRecipeLogic extends NoEnergySteamMultiblockRecipeLogic {
        public RoostRecipeLogic(MetaTileEntityRoost mte, int parallelLimit) {
            super(mte, parallelLimit);
        }
        @Override public @NotNull ParallelLogicType getParallelLogicType() {
            return ParallelLogicType.MULTIPLY;
        }
        @Override public void setMaxProgress(int maxProgress) {
            if(maxProgress > 60 * 20) {
                maxProgress = (int) Math.sqrt(60.0 * 20 * maxProgress);
            }
            this.maxProgressTime = maxProgress;
        }
    }

}
