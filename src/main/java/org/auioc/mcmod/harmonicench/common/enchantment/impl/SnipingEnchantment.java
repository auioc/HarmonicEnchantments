package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinAbstractArrow;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public class SnipingEnchantment extends AbstractHEEnchantment implements IProjectileEnchantment.HurtLiving, IProjectileEnchantment.AbstractArrow {

    public SnipingEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.CROSSBOW, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public int getMinCost(int lvl) {
        return lvl * 10 - 9;
    }

    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
            && other != Enchantments.QUICK_CHARGE
            && other != Enchantments.MULTISHOT
            && other != HEEnchantments.EFFICACY.get();
    }

    @Override
    public float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 shootingPosition, float amount) {
        double distance = shootingPosition.distanceTo(target.position());
        if (distance > 20.0D) {
            double m = 0.0D;
            for (int k = 1, n = lvl + 1; k < n; k++) m += (1.0D / (double) k) * ((distance - 20.0D) / 40.0D);
            return (float) (amount * (m + 1));
        }
        return amount;
    }

    @Override
    public void handleAbstractArrow(int lvl, AbstractArrow arrow) {
        var _arrow = ((IMixinAbstractArrow) arrow);
        _arrow.setGravity(_arrow.getGravity() * (1.0D - (Math.min(lvl, this.getMaxLevel()) * 0.11D)));
    }

}
