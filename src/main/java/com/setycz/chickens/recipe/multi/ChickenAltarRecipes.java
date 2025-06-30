package com.setycz.chickens.recipe.multi;

import com.setycz.chickens.api.recipe.ChickensRecipeMaps;
import com.setycz.chickens.item.ItemSpawnEgg;
import net.minecraft.init.Items;

import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class ChickenAltarRecipes {
    public static void init() {
        ChickensRecipeMaps.CHICKEN_ALTAR_RECIPES.recipeBuilder()
            .input(Items.WHEAT_SEEDS, 1)
            .chancedOutputs(Arrays.asList(new ChancedItemOutput[]{
                new ChancedItemOutput(ItemSpawnEgg.getSpawnEggById("chickens:flintchicken"), 2000, 0),
                new ChancedItemOutput(ItemSpawnEgg.getSpawnEggById("chickens:whitechicken"), 2000, 0),
                new ChancedItemOutput(ItemSpawnEgg.getSpawnEggById("chickens:logchicken"), 2000, 0),
                new ChancedItemOutput(ItemSpawnEgg.getSpawnEggById("chickens:sandchicken"), 2000, 0),
                new ChancedItemOutput(ItemSpawnEgg.getSpawnEggById("chickens:smartchicken"), 2000, 0)
            }))
            .EUt(1)
            .duration(400)
            .buildAndRegister();
    }
}
