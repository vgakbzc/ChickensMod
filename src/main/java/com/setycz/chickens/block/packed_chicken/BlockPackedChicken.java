package com.setycz.chickens.block.packed_chicken;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Random;

public class BlockPackedChicken extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private final ChickensRegistryItem chicken;

    public BlockPackedChicken(ChickensRegistryItem chicken) {
        super(Material.WOOD);
        this.chicken = chicken;
        setUnlocalizedName("packed." + chicken.getRegistryNameString());
        setRegistryName("packed_" + chicken.getRegistryNameString());
        setTickRandomly(true);
        setHardness(1.0F);
        setResistance(1F);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityPackedChicken te = new TileEntityPackedChicken();
        te.setMaxProgress(chicken.getMinLayTime() * 1.83105E-4f);
        te.setLayItem(chicken.getLayItemHolder().getItem());
        return te;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TileEntityPackedChicken) {
            ((TileEntityPackedChicken) tileEntity).dropContents();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        String modelResourceLocation = "" + getRegistryName();
        ModelLoader.setCustomModelResourceLocation(
            Item.getItemFromBlock(this), 0,
            new ModelResourceLocation(modelResourceLocation, "inventory")
        );
        FMLLog.info("ChickensModelLoader: Loading model " + modelResourceLocation);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        EnumFacing facing = EnumFacing.getFacingFromVector(
            (float) (entity.posX - clickedBlock.getX()),
            (float) (0.0),
            (float) (entity.posZ - clickedBlock.getZ()));
        if(facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return facing;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7).getAxis() == EnumFacing.Axis.Y ?
            EnumFacing.NORTH : EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
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
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TileEntityPackedChicken) {
            ((TileEntityPackedChicken) tileEntity).progress(1.0f);
//            FMLLog.info("PackedChickens: Random Tick at block pos %d %d %d", pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

}
