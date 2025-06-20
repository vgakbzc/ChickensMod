package com.setycz.chickens.jei.breeding;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by setyc on 21.02.2016.
 */
public class BreedingRecipeWrapper extends BlankRecipeWrapper {
    private final List<ItemStack> parents;
    private final ItemStack child;
    private final int chance;

    public BreedingRecipeWrapper(ItemStack parent1, ItemStack parent2, ItemStack child, int chance) {
        parents = new ArrayList<ItemStack>();
        parents.add(parent1);
        parents.add(parent2);
        this.child = child;
        this.chance = chance;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, parents);
        ingredients.setOutput(ItemStack.class, child);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String message = Translator.translateToLocalFormatted("gui.breeding.chance", chance/100, chance/10%10, chance%10);
        minecraft.fontRenderer.drawString(message, 32, 25, Color.gray.getRGB());
    }
}
