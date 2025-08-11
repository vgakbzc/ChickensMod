package com.setycz.chickens.registry;

import javax.annotation.Nullable;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.block.BlockInit;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.handler.ItemHolder;
import com.setycz.chickens.handler.SpawnType;

import com.setycz.chickens.item.ItemSpawnEgg;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import com.google.gson.JsonObject;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.material.Material;

import java.util.ArrayList;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensRegistryItem {
    private final ResourceLocation registryName;
    
    private final String entityName;
    private final String registryNameString;
    private ItemHolder layItem;
    private ItemHolder dropItem;
    private  int bgColor;
    private  int fgColor;
    private  ResourceLocation texture;
    private SpawnType spawnType;
    private boolean isEnabled = true;
    private float layCoefficient = 1.0f;
    public final int DEFAULT_WEIGHT = 100;

    private float rarity = 1.0f;
    private boolean defaultRarity = true;
    private float setedRarity = 0.0f;

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
        public JsonObject toJsonObject(){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent1", parent1.getRegistryName().toString());
            jsonObject.addProperty("parent2", parent2.getRegistryName().toString());
            jsonObject.addProperty("weight", weight);
            return jsonObject;
        }
    }
    private ArrayList<BreedHelper> parents = new ArrayList<BreedHelper>();

    public class GTItemHolder {
        private OrePrefix orePrefix;
        private Material material;
        public GTItemHolder(OrePrefix orePrefix, Material material) {
            this.orePrefix = orePrefix;
            this.material = material;
        }
        public ItemStack getItem(){ return OreDictUnifier.get(orePrefix, material); }
    }
    private GTItemHolder gtItemHolder = null;

    @Deprecated
    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemStack layItem, int bgColor, int fgColor) {
        this(registryName, entityName, texture, layItem, bgColor, fgColor, null, null);
    }
    @Deprecated
    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemStack layItem, int bgColor, int fgColor, @Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2)   {
    	this(registryName, entityName, texture, new ItemHolder(layItem, false), bgColor, fgColor, parent1, parent2);
    }
    public ChickensRegistryItem(String entityName) {
        this.registryName = new ResourceLocation(ChickensMod.MODID, entityName);
        this.entityName = entityName;
        layItem = null;
        dropItem = null;
        this.bgColor = 0xffffff;
        this.fgColor = 0x000000;
        // this.texture = new ResourceLocation("chickens", "textures/entity/flint_chicken.png");
        this.spawnType = SpawnType.NONE;
        this.setNoParents();

        String texturePath = "";
        if(entityName.length() > 0) {
            if(entityName.charAt(0) >= 'A' && entityName.charAt(0) <= 'A') {
                entityName = (entityName.charAt(0) + (int)'a' - (int)'A') + entityName.substring(1);
            }
        }
        for(int i = 0; i < entityName.length(); i++ ) {
            if (i != 0 && entityName.charAt(i) >= 'A' && entityName.charAt(i) <= 'Z') {
                texturePath += "_" + (char) ((int) entityName.charAt(i) + (int) 'a' - (int) 'A');
            } else {
                texturePath += entityName.charAt(i);
            }
        }
        this.registryNameString = texturePath;
        texturePath = "textures/entity/" + texturePath + ".png";
        this.texture = new ResourceLocation("chickens", texturePath);
    }

    @Deprecated
    public ChickensRegistryItem(ResourceLocation registryName, String entityName, ResourceLocation texture, ItemHolder layItem, int bgColor, int fgColor, @Nullable ChickensRegistryItem parent1, @Nullable ChickensRegistryItem parent2) {
        this.registryName = registryName;
        this.entityName = entityName;
        this.layItem = layItem;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.texture = texture;
        this.spawnType = SpawnType.NORMAL;
        this.addParents(parent1, parent2);

        String texturePath = "";
        for(int i = 0; i < entityName.length(); i++ ) {
            if (i != 0 && entityName.charAt(i) >= 'A' && entityName.charAt(i) <= 'Z') {
                texturePath += "_" + (char) ((int) entityName.charAt(i) + (int) 'a' - (int) 'A');
            } else {
                texturePath += entityName.charAt(i);
            }
        }
        this.registryNameString = texturePath;
    }

    public String getRegistryNameString() { return registryNameString; }

    public ItemHolder getDropItemHolder() {
    	return this.dropItem == null ? this.layItem : this.dropItem;
    }
    
    public ItemHolder getLayItemHolder() {
    	return this.layItem;
    }

    public void registerGTItemHolder() {
        if(gtItemHolder != null) {
            this.setLayItem(gtItemHolder.getItem());
        }
    }

    public void setGtItemHolder(OrePrefix prefix, Material material) {
        gtItemHolder = new GTItemHolder(prefix, material);
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

        float parentRarity = parent1.getRarity() + parent2.getRarity() - Math.min(parent1.getRarity(), parent2.getRarity()) / 2;
        this.rarity = Math.max(this.rarity, parentRarity);

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

    public float getRarity(){
        if(!this.defaultRarity) {
            return Math.max(1.0f, this.setedRarity);
        }
        return Math.max(this.rarity, 1.0f);
    }
    public void setRarity(float rarity) {
        this.defaultRarity = false;
        this.setedRarity = rarity;
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

    public ItemStack getSpawnEgg() { return getSpawnEgg(1); }
    public ItemStack getSpawnEgg(int amount) { return ItemSpawnEgg.getSpawnEggById(ChickensMod.MODID + ":" + entityName.toLowerCase(), amount); }

    public int getMinLayTime() {
        return (int) Math.min(Math.max(6000 * getRarity() * layCoefficient, 1.0f), 1e9f);
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
        if(dropItem == null) {
            dropItem = itemHolder;
        }
        return this;
    }

    @Deprecated
    public ChickensRegistryItem setLayItem(ItemStack itemstack) {
    	setLayItem(new ItemHolder(itemstack, false));
    	return this;
    }
    
    public ChickensRegistryItem setNoParents() {
        parents = new ArrayList<BreedHelper>();
        this.rarity = 1.0f;
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

    public void setTextureResourceLocation(ResourceLocation res) {
        this.texture = res;
    }
    public void setFgColor(int color) {
        this.fgColor = color;
    }
    public void setBgColor(int color) {
        this.bgColor = color;
    }
}
