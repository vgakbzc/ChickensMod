package com.setycz.chickens.block.packed_chicken;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.api.IHasCustomItemBlock;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.item.ItemSpawnEgg;
import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Random;

public class BlockPackedChicken extends BlockContainer implements IHasCustomItemBlock {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private final ChickensRegistryItem chicken;
    private ItemBlockPackedChicken itemBlock = null;

    public ItemBlock getCustomItemBlock() {
        if(itemBlock == null) {
            itemBlock = new ItemBlockPackedChicken(this);
        }
        return itemBlock;
    }

    @Override
    public String getLocalizedName() {
        String chickenName = "";
        if(I18n.hasKey(this.getChicken().getTranslationKeyName())){
            chickenName = I18n.format(this.getChicken().getTranslationKeyName());
        } else {
            chickenName = I18n.format("entity.chicken.name", this.getChicken().getLayItemHolder().getStack().getDisplayName());
        }
        return I18n.format("tile.packed.name", chickenName);
    }

    public BlockPackedChicken(ChickensRegistryItem chicken) {
        super(Material.WOOD);
        this.chicken = chicken;
        setUnlocalizedName("packed." + chicken.getEntityName());
        setRegistryName("packed_" + chicken.getEntityName());
        setHardness(1.0F);
        setResistance(1F);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityPackedChicken te = new TileEntityPackedChicken();
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
            Item.getItemFromBlock(this), 0, (new ModelResourceLocation(modelResourceLocation, "inventory"))
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public ChickensRegistryItem getChicken() {
        return chicken;
    }
}
