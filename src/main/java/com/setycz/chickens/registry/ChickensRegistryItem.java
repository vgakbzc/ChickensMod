package com.setycz.chickens.registry;

import javax.annotation.Nullable;

import com.setycz.chickens.handler.ItemHolder;
import com.setycz.chickens.handler.SpawnType;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensRegistryItem {
    private final ResourceLocation registryName;
    
    private final String entityName;
    private ItemHolder layItem;
    private ItemHolder dropItem;
    private final int bgColor;
    private final int fgColor;
    private final ResourceLocation texture;
    private SpawnType spawnType;
    private boolean isEnabled = true;
    private float layCoefficient = 1.0f;
    public final int DEFAULT_WEIGHT = 100;

    private int tier = 0;

    public class BreedHelper {
        private final ChickensRegistryItem parent1;
        private final ChickensRegistryItem parent2;
        private final int weight;

        public BreedHelper(ChickensRegistryItem parent1, ChickensRegistryItem parent2){
            this(parent1, parent2, DEFAULT_WEIGHT);
        }
        public BreedHelper(ChickensRegistryItem parent1, ChickensRegistryItem parent2, int weight){
            this.parent1 = parent1;
            this.parent2 = parent2;
            this.weight = weight;
        }
        public boolean isBreedable(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
            return this.parent1 == parent1 && this.parent2 == parent2 || this.parent1 == parent2 && this.parent2 == parent1;
        }
        public int getWeight(){
            return weight;
        }
        public ChickensRegistryItem getParent1() { return parent1; }
        public ChickensRegistryItem getParent2() { return parent2; }
    }
    private ArrayList<BreedHelper> parents = new ArrayList<BreedHelper>();

    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemStack layItem, int bgColor, int fgColor) {
        this(registryName, entityName, texture, layItem, bgColor, fgColor, null, null);
    }
    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemStack layItem, int bgColor, int fgColor, @Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2)   {
    	this(registryName, entityName, texture, new ItemHolder(layItem, false), bgColor, fgColor, parent1, parent2);
    }
    
    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemHolder layItem, int bgColor, int fgColor, @Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2) {
        this.registryName = registryName;
        this.entityName = entityName;
        this.layItem = layItem;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.texture = texture;
        this.spawnType = SpawnType.NORMAL;
        this.addParents(parent1, parent2);
    }


    public ItemHolder getDropItemHolder() {
    	return this.dropItem == null ? this.layItem : this.dropItem;
    }
    
    public ItemHolder getLayItemHolder() {
    	return this.layItem;
    }

    public ArrayList<BreedHelper> getParents() {
        return parents;
    }

    public ChickensRegistryItem addParents(@Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2, int weight){

        if (parent1 == null || parent2 == null) {
            return this;
        }

        BreedHelper breed = new BreedHelper(parent1, parent2, weight);
        parents.add(breed);

        return this;

    }

    public ChickensRegistryItem addParents(@Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2) {
        addParents(parent1, parent2, DEFAULT_WEIGHT);
        return this;
    }

    public ChickensRegistryItem setDropItem(ItemHolder itemHolder) {
        dropItem = itemHolder;
        return this;
    }
    
    @Deprecated
    public ChickensRegistryItem setDropItem(ItemStack itemstack) {
    	return setDropItem(new ItemHolder(itemstack, false));
    }

    public ChickensRegistryItem setSpawnType(SpawnType type) {
        spawnType = type;
        return this;
    }

    public ChickensRegistryItem setLayCoefficient(float coef) {
        layCoefficient = coef;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getBgColor() {
        return bgColor;
    }

    public int getFgColor() {
        return fgColor;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public ItemStack createLayItem() {
        return layItem.getStack();
    }

    public int getTier(){
        return parents.size() == 0 ? 1 : 2;
    }
    

    public ItemStack createDropItem() {
        if (dropItem != null) {
            return dropItem.getStack();
        }
        return createLayItem();
    }

    public int isChildOf(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        BreedHelper parentsArray[] = parents.toArray(new BreedHelper[0]);
        for(BreedHelper breed : parentsArray) {
            if(breed.isBreedable(parent1, parent2)) {
                return breed.getWeight();
            }
        }
        return 0;
    }

    public boolean isDye() {
        return layItem.getItem() == Items.DYE;
    }

    public boolean isDye(int dyeMetadata) {
        return layItem.getItem() == Items.DYE && layItem.getMeta() == dyeMetadata;
    }

    public int getDyeMetadata() {
        return layItem.getMeta();
    }

    public boolean canSpawn() {
        // return getTier() == 1 && spawnType != SpawnType.NONE;
        return false;
    }

//    public int getId() {
//        return id;
//    }
    
    public ResourceLocation getRegistryName(){
    	return registryName;
    }

    public int getMinLayTime() {
        return (int) Math.max(6000 * getTier() * layCoefficient, 1.0f);
    }

    public int getMaxLayTime() {
        return 2 * getMinLayTime();
    }

    public SpawnType getSpawnType() {
        return spawnType;
    }

    public boolean isImmuneToFire() {
        return spawnType == SpawnType.HELL;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public ChickensRegistryItem setLayItem(ItemHolder itemHolder) {
        layItem = itemHolder;
        return this;
    }

    @Deprecated
    public ChickensRegistryItem setLayItem(ItemStack itemstack) {
    	setLayItem(new ItemHolder(itemstack, false));
    	return this;
    }
    
    public ChickensRegistryItem setNoParents() {
        parents = new ArrayList<BreedHelper>();
        return this;
    }

    public ChickensRegistryItem setParentsNew(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        this.setNoParents();
        this.addParents(parent1, parent2);
        return this;
    }

    @Deprecated
    public void setParents(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        this.setNoParents();
        this.addParents(parent1, parent2);
    }

    public boolean isBreedable() {
        return parents.size() > 0;
    }
    
    
    public static void registerChickens()
    {
    Item.getByNameOrId("");	
    }
}
