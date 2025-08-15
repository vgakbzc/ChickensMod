package com.setycz.chickens.metatileentities.multi;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.metatileentities.multi.steam.MetaTileEntityChickenAltar;
import com.setycz.chickens.metatileentities.multi.steam.MetaTileEntityRoost;
import magicbook.gtlitecore.api.utils.MultiblockRegistryHelper;
import net.minecraft.util.ResourceLocation;

public class ChickensMetaTileEntities {
//    public static MetaTileEntityChickenAltar CHICKEN_ALTAR;
//    public static MetaTileEntityRoost ROOST;

    public static void registerMultiblockMachines() {
//        CHICKEN_ALTAR = MultiblockRegistryHelper.registerMultiMetaTileEntity(300, new MetaTileEntityChickenAltar(new ResourceLocation(ChickensMod.MODID, "chicken_altar")));
//        ROOST = MultiblockRegistryHelper.registerMultiMetaTileEntity(301, new MetaTileEntityRoost(new ResourceLocation(ChickensMod.MODID, "roost")));
    }

    public static void register() {
        registerMultiblockMachines();
    }
}
