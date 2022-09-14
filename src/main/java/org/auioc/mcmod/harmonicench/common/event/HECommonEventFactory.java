package org.auioc.mcmod.harmonicench.common.event;

import org.auioc.mcmod.harmonicench.common.event.impl.LivingEatEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class HECommonEventFactory {

    private static final IEventBus BUS = MinecraftForge.EVENT_BUS;

    public static LivingEatEvent onLivingEat(LivingEntity living, FoodData foodData, ItemStack foodItemStack) {
        var event = new LivingEatEvent(living, foodData, foodItemStack);
        BUS.post(event);
        return event;
    }

}
