package com.setycz.chickens.block.breeder;

import com.setycz.chickens.block.packed_chicken.TileEntityPackedChicken;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import scala.reflect.internal.Mode;

public class BlockBreeder extends BlockContainer {

    public BlockBreeder() {
        super(Material.ROCK);
        setRegistryName("breeder");
        setUnlocalizedName("breeder");
        setHardness(1.0F);
        setResistance(1F);
        setHarvestLevel("Pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityBreeder te = new TileEntityBreeder();
        return te;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(getRegistryName().toString(), "inventory")
        );
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
