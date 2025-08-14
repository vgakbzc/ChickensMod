package com.setycz.chickens.block;

import com.setycz.chickens.block.accelerators.BlockAccelerator;
import com.setycz.chickens.block.breeder.BlockBreeder;
import com.setycz.chickens.block.packed_chicken.BlockPackedChicken;
import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockInit {

    public static class PackedChickens{
        public static HashMap<ChickensRegistryItem, BlockPackedChicken> blockPackedChickens = new HashMap<ChickensRegistryItem, BlockPackedChicken>();
        public static void add(ChickensRegistryItem chicken) {
            BlockPackedChicken block = new BlockPackedChicken(chicken);
            blockPackedChickens.put(chicken, block);
        }

        @SideOnly(Side.CLIENT)
        public static void initModels() {
            for(BlockPackedChicken packedChicken : blockPackedChickens.values()) {
                packedChicken.initModel();
            }
        }

        public static BlockPackedChicken getBlockByChicken(ChickensRegistryItem chicken) {
            return blockPackedChickens.get(chicken);
        }
    }

    public static class Accelerators{
        public static BlockAccelerator[] accelerators = {
                new BlockAccelerator("accelerator_stone", 1.0f),
                new BlockAccelerator("accelerator_iron", 3.0f),
                new BlockAccelerator("accelerator_gold", 7.0f),
                new BlockAccelerator("accelerator_diamond", 15.0f)
        };
        @SideOnly(Side.CLIENT)
        public static void initModels() {
            for(BlockAccelerator block : accelerators) {
                block.initModel();
            }
        }
    }

    public static final BlockBreeder BREEDER = new BlockBreeder();

    public static List<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.addAll(PackedChickens.blockPackedChickens.values());
        blocks.addAll(Arrays.asList(Accelerators.accelerators));
        blocks.add(BREEDER);
        return blocks;
    }

    public static void initModels() {
        PackedChickens.initModels();
        Accelerators.initModels();
        BREEDER.initModel();
    }

}
