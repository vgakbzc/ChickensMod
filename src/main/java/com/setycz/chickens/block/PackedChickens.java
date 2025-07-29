package com.setycz.chickens.block;

import com.setycz.chickens.block.packed_chicken.BlockPackedChicken;
import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class PackedChickens {
    public static HashMap<ChickensRegistryItem, BlockPackedChicken> blockPockedChickens = new HashMap<ChickensRegistryItem, BlockPackedChicken>();
    public static void add(ChickensRegistryItem chicken) {
        BlockPackedChicken block = new BlockPackedChicken(chicken);
        blockPockedChickens.put(chicken, block);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for(BlockPackedChicken packedChicken : blockPockedChickens.values()) {
            packedChicken.initModel();
        }
    }
}
