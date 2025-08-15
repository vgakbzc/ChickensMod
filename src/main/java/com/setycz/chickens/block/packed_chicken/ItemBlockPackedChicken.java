package com.setycz.chickens.block.packed_chicken;

import com.setycz.chickens.registry.ChickensRegistryItem;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPackedChicken extends ItemBlock {

    private final ChickensRegistryItem CHICKEN;

    public ItemBlockPackedChicken(BlockPackedChicken block) {
        super(block);
        CHICKEN = block.getChicken();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return block.getLocalizedName();
    }

}
