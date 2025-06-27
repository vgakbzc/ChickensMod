package com.setycz.chickens.api.recipe;

import gregtech.api.GTValues;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.MultiblockShapeInfo;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.recipes.builders.FuelRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.machines.RecipeMapAssemblyLine;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockFireboxCasing;
import gregtech.core.sound.GTSoundEvents;

public class ChickensRecipeMaps {
    public static final RecipeMap<SimpleRecipeBuilder> CHICKEN_ALTAR_RECIPES = new RecipeMap<>("chicken_altar", 4, 16, 0, 0, new SimpleRecipeBuilder(), false)
        .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, ProgressWidget.MoveType.HORIZONTAL)
        .setSound(GTSoundEvents.SCIENCE);
}
