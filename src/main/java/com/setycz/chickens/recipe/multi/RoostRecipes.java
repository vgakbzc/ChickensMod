package com.setycz.chickens.recipe.multi;

import com.setycz.chickens.api.recipe.ChickensRecipeMaps;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;

import java.util.ArrayList;

public class RoostRecipes {
    public static void init() {
        ArrayList<ChickensRegistryItem> chickens = new ArrayList<>(ChickensRegistry.getItems());
        for(ChickensRegistryItem chicken : chickens) {
            ItemStack chickenEgg = chicken.getSpawnEgg();
            ItemStack chickenLayItem = chicken.getLayItemHolder().getStack();
            final int fakeEUt = (int)Math.min(2e9, 4 * chicken.getRarity() + 1);
            if(fakeEUt >= 32) {
                continue;
            }
            ChickensRecipeMaps.ROOST_RECIPES.recipeBuilder()
                .input(Items.WHEAT_SEEDS, 1)
                .input(new GTRecipeItemInput(chickenEgg))
                .outputs(chickenEgg, chickenLayItem)
                .EUt(1)
                .duration((int)Math.min(2e9, (35 * Math.sqrt(chicken.getRarity() * 10) + 100) * Math.sqrt(fakeEUt * 0.1)))
                .buildAndRegister();
        }
    }
}
