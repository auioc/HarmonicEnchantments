package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class IceAspectEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.Hurt {

    public IceAspectEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND, 2);
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 + 20 * (lvl - 1);
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FIRE_ASPECT;
    }

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (isSource && target.canFreeze()) {
            int ticksFrozen = target.getTicksFrozen();
            double r = 0.0D;
            double f = (ticksFrozen == 0) ? 200.0D : 100.0D;
            for (int k = 1, n = lvl + 1; k < n; k++) r += f / ((double) k);
            target.setTicksFrozen(ticksFrozen + ((int) r));
        }
        return amount;
    }

}
