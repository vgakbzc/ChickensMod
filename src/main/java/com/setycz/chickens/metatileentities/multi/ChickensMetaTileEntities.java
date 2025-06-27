package com.setycz.chickens.metatileentities.multi;

import com.setycz.chickens.metatileentities.multi.steam.MetaTileEntityChickenAltar;
import magicbook.gtlitecore.api.utils.MultiblockRegistryHelper;
import net.minecraft.util.ResourceLocation;

public class ChickensMetaTileEntities {
    public static MetaTileEntityChickenAltar CHICKEN_ALTAR;

    public static void registerMultiblockMachines() {
        CHICKEN_ALTAR = MultiblockRegistryHelper.registerMultiMetaTileEntity(300, new MetaTileEntityChickenAltar(new ResourceLocation("chickens", "chicken_altar")));
    }

    public static void register() {
        registerMultiblockMachines();
    }
}
