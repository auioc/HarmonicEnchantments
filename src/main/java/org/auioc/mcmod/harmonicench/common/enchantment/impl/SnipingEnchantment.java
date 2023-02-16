package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinAbstractArrow;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public class SnipingEnchantment extends HEEnchantment implements IProjectileEnchantment.HurtLiving, IProjectileEnchantment.AbstractArrow {

    public SnipingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.CROSSBOW,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.QUICK_CHARGE
                && o != Enchantments.MULTISHOT
                && o != HEEnchantments.EFFICACY.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return lvl * 10 - 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
    }

    @Override
    public float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 shootingPosition, float amount) {
        double distance = shootingPosition.distanceTo(target.position());
        if (distance > 20.0D) {
            double m = MathUtil.sigma(lvl, 1, (double i) -> (1.0D / i) * ((distance - 20.0D) / 40.0D));
            return (float) (amount * (m + 1));
        }
        return amount;
    }

    @Override
    public void handleAbstractArrow(int lvl, AbstractArrow arrow) {
        ((IMixinAbstractArrow) arrow).setGravityOffset(0.05F * (1.0F - (Mth.clamp(this.getMaxLevel() - lvl + 1, 1, this.getMaxLevel()) * 0.15F)));
    }

}
