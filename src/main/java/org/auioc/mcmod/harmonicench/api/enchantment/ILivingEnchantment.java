package org.auioc.mcmod.harmonicench.api.enchantment;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ILivingEnchantment {

    public static interface Death {

        void onLivingDeath(int lvl, LivingEntity target, DamageSource source);

    }

    public static interface Hurt {

        float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount);

    }

    public static interface FoodData {

        Pair<Integer, Float> onLivingEat(int lvl, ItemStack itemStack, EquipmentSlot slot, LivingEntity living, ItemStack foodItemStack, int nutrition, float saturationModifier);

    }

}
