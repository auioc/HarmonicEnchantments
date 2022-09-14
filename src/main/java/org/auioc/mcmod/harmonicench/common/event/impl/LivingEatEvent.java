package org.auioc.mcmod.harmonicench.common.event.impl;

import org.auioc.mcmod.harmonicench.api.event.FoodDataEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

public class LivingEatEvent extends FoodDataEvent {

    private final ItemStack foodItemStack;
    private int nutrition = 0;
    private float saturationModifier = 0.0F;

    public LivingEatEvent(LivingEntity living, FoodData foodData, ItemStack foodItemStack) {
        super(living, foodData);
        this.foodItemStack = foodItemStack;
        if (foodItemStack.isEdible()) {
            var foodProperties = foodItemStack.getFoodProperties(living);
            this.nutrition = foodProperties.getNutrition();
            this.saturationModifier = foodProperties.getSaturationModifier();
        }
    }

    public ItemStack getFoodItemStack() {
        return this.foodItemStack;
    }

    public int getNutrition() {
        return this.nutrition;
    }

    public float getSaturationModifier() {
        return this.saturationModifier;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public void setSaturationModifier(float saturationModifier) {
        this.saturationModifier = saturationModifier;
    }

}
