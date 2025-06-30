package com.setycz.chickens.metatileentities.multi.steam;

import com.setycz.chickens.api.recipe.ChickensRecipeMaps;
import com.sun.istack.internal.NotNull;
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

import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityChickenAltar extends NoEnergyMultiblockController {

    private static final int PARALLEL_LIMIT = 64;

    public MetaTileEntityChickenAltar(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, ChickensRecipeMaps.CHICKEN_ALTAR_RECIPES);
        this.recipeMapWorkable = new ChickenAltarRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityChickenAltar(metaTileEntityId);
    }

    @NotNull @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
            .aisle("     AAAAA     ", "     B   B     ", "               ", "               ", "               ")
            .aisle("   AACCCCCAA   ", "     D   D     ", "               ", "               ", "               ")
            .aisle("  ACCCCCCCCCA  ", "  B  D   D  B  ", "  B         B  ", "  E         E  ", "               ")
            .aisle(" ACCCCCCCCCCCA ", "    CCCCCCC    ", "     D   D     ", "               ", "               ")
            .aisle(" ACCCCCCCCCCCA ", "   CCCCCCCCC   ", "     D   D     ", "               ", "               ")
            .aisle("ACCCCBCCCBCCCCA", "BFFCCBCCCBCCFFB", "   FFBCCCBFF   ", "     B   B     ", "     E   E     ")
            .aisle("ACCCCCCCCCCCCCA", "   CCCCCCCCC   ", "     CCCCC     ", "      GGG      ", "               ")
            .aisle("ACCCCCCCCCCCCCA", "   CCCCCCCCC   ", "     CCCCC     ", "      GGG      ", "               ")
            .aisle("ACCCCCCCCCCCCCA", "   CCCCCCCCC   ", "     CCCCC     ", "      GGG      ", "               ")
            .aisle("ACCCCBCCCBCCCCA", "BFFCCBCCCBCCFFB", "   FFBCCCBFF   ", "     B   B     ", "     E   E     ")
            .aisle(" ACCCCCCCCCCCA ", "   CCCCCCCCC   ", "     D   D     ", "               ", "               ")
            .aisle(" ACCCCCCCCCCCA ", "    CCCCCCC    ", "     D   D     ", "               ", "               ")
            .aisle("  ACCCCCCCCCA  ", "  B  D   D  B  ", "  B         B  ", "  E         E  ", "               ")
            .aisle("   AACCCCCAA   ", "     D   D     ", "               ", "               ", "               ")
            .aisle("     AAHAA     ", "     B   B     ", "               ", "               ", "               ")
            .where('H', this.selfPredicate())
            .where('B', getLogStates())
            .where('C', getPlanksStates())
            .where('D', getLogStates())
            .where('E', states(Blocks.GLOWSTONE.getDefaultState()))
            .where('F', getLogStates())
            .where('G', states(Blocks.HAY_BLOCK.getDefaultState()))
            .where('A', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.COKE_BRICKS))
                .setMinGlobalLimited(11)
                .or(autoAbilities(false, true, true, false, false, false)))
            .build();
    }
    private TraceabilityPredicate getLogStates() {
        TraceabilityPredicate state = states(Blocks.LOG.getStateFromMeta(0));
        for(int i = 1; i <= 11; i++ ) {
            state = state.or(states(Blocks.LOG.getStateFromMeta(i)));
        }
        for(int j = 0; j <= 8; j += 4) {
            for(int i = 0; i <= 1; i++ ) {
                state = state.or(states(Blocks.LOG2.getStateFromMeta(i + j)));
            }
        }
        return state;
    }
    private TraceabilityPredicate getPlanksStates() {
        TraceabilityPredicate state = states(Blocks.PLANKS.getStateFromMeta(0));
        for(int i = 1; i <= 5; i++ ) {
            state = state.or(states(Blocks.PLANKS.getStateFromMeta(i)));
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

    public class ChickenAltarRecipeLogic extends NoEnergyMultiblockRecipeLogic {

        public ChickenAltarRecipeLogic(NoEnergyMultiblockController tileEntity) {
            super(tileEntity, ChickensRecipeMaps.CHICKEN_ALTAR_RECIPES);
        }

        @Override
        public int getParallelLimit() { return PARALLEL_LIMIT; }

    }

}
