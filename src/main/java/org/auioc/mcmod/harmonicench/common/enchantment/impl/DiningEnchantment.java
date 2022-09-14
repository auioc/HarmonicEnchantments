package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class DiningEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.FoodData {

    public DiningEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int lvl) {
        return 25;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 50;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
            && other != Enchantments.MENDING
            && other != Enchantments.INFINITY_ARROWS
            && other != HEEnchantments.BLESSING.get();
    }

    @Override
    public Pair<Integer, Float> onLivingEat(int lvl, ItemStack itemStack, EquipmentSlot slot, LivingEntity living, ItemStack foodItemStack, int nutrition, float saturationModifier) {
        if (itemStack.isDamaged() && nutrition > 0) {
            int damage = itemStack.getDamageValue();
            if (nutrition >= damage) {
                nutrition -= damage;
                damage = 0;
            } else {
                nutrition = 0;
                damage -= nutrition;
            }
            itemStack.setDamageValue(damage);
        }
        return Pair.of(nutrition, saturationModifier);
    }

}
