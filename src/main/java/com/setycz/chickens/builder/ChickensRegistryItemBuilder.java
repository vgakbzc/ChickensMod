package com.setycz.chickens.builder;

import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ChickensRegistryItemBuilder{

    private ChickensRegistryItem chicken;
    public ChickensRegistryItemBuilder(String entityId) {
        chicken = new ChickensRegistryItem(entityId);
    }

    public ChickensRegistryItem build() { return chicken; }

    public ChickensRegistryItemBuilder addParents(ChickensRegistryItem parent1, ChickensRegistryItem parent2, int weight) {
        chicken.addParents(parent1, parent2, weight);
        return this;
    }
    public ChickensRegistryItemBuilder addParents(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        return this.addParents(parent1, parent2, chicken.DEFAULT_WEIGHT);
    }

    public ChickensRegistryItemBuilder setTextureLocation(String texture) {
        chicken.setTextureResourceLocation(new ResourceLocation("/texture/entity" + texture));
        return this;
    }

    public ChickensRegistryItemBuilder setFgColor(int color) {
        chicken.setFgColor(color);
        return this;
    }
    public ChickensRegistryItemBuilder setBgColor(int color) {
        chicken.setBgColor(color);
        return this;
    }
    public ChickensRegistryItemBuilder setColor(int bgColor, int fgColor) {
        return this.setFgColor(fgColor).setBgColor(bgColor);
    }
    public ChickensRegistryItemBuilder setDropItem(ItemStack item) {
        chicken.setDropItem(item);
        return this;
    }
    public ChickensRegistryItemBuilder setLayItem(ItemStack item) {
        chicken.setLayItem(item);
        return this;
    }

}
