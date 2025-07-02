package com.setycz.chickens.item;

import javax.annotation.Nullable;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by setyc on 12.02.2016.
 */
@SuppressWarnings("deprecation")
public class ItemSpawnEgg extends Item implements IColorSource {
    public ItemSpawnEgg() {
        setHasSubtypes(true);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab))
        {
        	for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
        		ItemStack itemstack = new ItemStack(this, 1);
        		applyEntityIdToItemStack(itemstack, chicken.getRegistryName()); 
        		subItems.add(itemstack);
        	}
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getTypeFromStack(stack));
        if(chickenDescription == null) return "null";
        return I18n.translateToLocal("entity." + chickenDescription.getEntityName() + ".name");
    }


    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getTypeFromStack(stack));
        if(chickenDescription == null) return 0000000;
        return renderPass == 0 ? chickenDescription.getBgColor() : chickenDescription.getFgColor();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(hand);
            BlockPos correlatedPos = correctPosition(pos, facing);
            activate(stack, worldIn, correlatedPos, stack.getMetadata());
            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    private BlockPos correctPosition(BlockPos pos, EnumFacing side) {
        final int[] offsetsXForSide = new int[]{0, 0, 0, 0, -1, 1};
        final int[] offsetsYForSide = new int[]{-1, 1, 0, 0, 0, 0};
        final int[] offsetsZForSide = new int[]{0, 0, -1, 1, 0, 0};

        int posX = pos.getX() + offsetsXForSide[side.ordinal()];
        int posY = pos.getY() + offsetsYForSide[side.ordinal()];
        int posZ = pos.getZ() + offsetsZForSide[side.ordinal()];

        return new BlockPos(posX, posY, posZ);
    }

    private void activate(ItemStack stack, World worldIn, BlockPos pos, int metadata) {
        ResourceLocation entityName = new ResourceLocation(ChickensMod.MODID, ChickensMod.CHICKEN);
        EntityChickensChicken entity = (EntityChickensChicken) EntityList.createEntityByIDFromName(entityName, worldIn);
        if (entity == null) {
            return;
        }
        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
        entity.setChickenType(getTypeFromStack(stack));

        NBTTagCompound stackNBT = stack.getTagCompound();
        if (stackNBT != null) {
            NBTTagCompound entityNBT = entity.writeToNBT(new NBTTagCompound());
            entityNBT.merge(stackNBT);
            entity.readEntityFromNBT(entityNBT);
        }

        worldIn.spawnEntity(entity);
    }
    
    
    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId)
    {
        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", entityId.toString());
        nbttagcompound.setTag("ChickenType", nbttagcompound1);
        stack.setTagCompound(nbttagcompound);
    }
    
    
    /**
     * Applies the data in the EntityTag tag of the given ItemStack to the given Entity.
     * @return 
     */
    @Nullable
    public static String getTypeFromStack(ItemStack stack)
    {
    	NBTTagCompound nbttagcompound = stack.getTagCompound();

    	if (nbttagcompound != null && nbttagcompound.hasKey("ChickenType", 10))
    	{
    		new NBTTagCompound();
    		NBTTagCompound chickentag = nbttagcompound.getCompoundTag("ChickenType");
    		
    		return chickentag.getString("id");
    	}
    	
    	return null;
    }

    @Nullable
    public static ItemStack getSpawnEggById(String id) {
        return getSpawnEggById(id, 1);
    }

    @Nullable
    public static ItemStack getSpawnEggById(String id, int amount) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        NBTTagCompound nbtTagCompoundInner = new NBTTagCompound();
        nbtTagCompoundInner.setString("id", id);
        nbtTagCompound.setTag("ChickenType", nbtTagCompoundInner);
        return GameRegistry.makeItemStack("chickens:spawn_egg", 0, amount, nbtTagCompound.toString());
    }
    
   
}
