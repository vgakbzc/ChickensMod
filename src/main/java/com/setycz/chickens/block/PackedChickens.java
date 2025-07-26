package com.setycz.chickens.block;

import com.setycz.chickens.block.packed_chicken.PackedChickenBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class PackedChickens {
    public static HashMap<String, PackedChickenBlock> registryNameToBlock = new HashMap<String, PackedChickenBlock>();
    public static void add(String chickenRegistryName) {
        PackedChickenBlock block = new PackedChickenBlock(chickenRegistryName);
        registryNameToBlock.put(chickenRegistryName, block);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for(PackedChickenBlock packedChicken : registryNameToBlock.values()) {
            packedChicken.initModel();
        }
    }
}
