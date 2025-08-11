package com.setycz.chickens.block.accelerators;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAccelerator extends Block {

    private final float mult;

    public BlockAccelerator(String registryName, float mult) {
        super(Material.ROCK);
        this.mult = mult;
        setHardness((float) Math.log(mult + 2));
        setRegistryName(registryName);
        setUnlocalizedName(registryName);
    }

    public float getAccelerationMultiplier() { return mult; }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        String modelResourceLocation = "" + getRegistryName();
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this), 0, (new ModelResourceLocation(modelResourceLocation, "normal"))
        );
        FMLLog.info("ChickensModelLoader: Loading model " + modelResourceLocation);
    }

}
