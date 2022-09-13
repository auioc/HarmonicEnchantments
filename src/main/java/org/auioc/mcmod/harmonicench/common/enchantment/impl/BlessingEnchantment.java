package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class BlessingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Protection {

    public BlessingEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.MENDING;
    }

    @Override
    public int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source) {
        if (source.isMagic()) {
            double l = 0.0D;
            int N = EnchantmentHelper.getEnchantments(itemStack).values().stream().mapToInt((i) -> i).sum();
            for (int i = 1, n = N + 1; i < n; ++i) l += 3.0D / (2.0D * ((double) i) - 1.0D);

            double r = 0.0D;
            for (int j = 1, n = lvl + 1; j < n; ++j) r += 1.0D / (3.0D * ((double) j) - 2.0D);

            return (int) (l * r);
        }
        return 0;
    }

}
