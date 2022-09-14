package org.auioc.mcmod.harmonicench.mixin.common;

import javax.annotation.Nullable;
import org.auioc.mcmod.harmonicench.common.event.HECommonEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(value = FoodData.class)
public class MixinFoodData {

    @Overwrite(remap = false)
    public void eat(Item p_38713_, ItemStack p_38714_, @Nullable LivingEntity entity) {
        var event = HECommonEventFactory.onLivingEat(entity, ((FoodData) (Object) this), p_38714_);
        if (!event.isCanceled()) {
            ((FoodData) (Object) this).eat(event.getNutrition(), event.getSaturationModifier());
        }
    }

}
