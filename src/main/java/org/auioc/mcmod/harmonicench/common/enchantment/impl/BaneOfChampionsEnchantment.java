package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BaneOfChampionsEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.Hurt {

    public BaneOfChampionsEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    public int getMinCost(int lvl) {
        return 5 + (lvl - 1) * 8;
    }

    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 20;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other instanceof DamageEnchantment;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem ? true : super.canEnchant(itemStack);
    }

    @Override
    public float onLivingHurt(int lvl, LivingEntity target, DamageSource source, float amount) {
        if (target.getHealth() <= 50.0F) return amount;
        return amount + ((float) lvl) * 2.5F;
    }

}