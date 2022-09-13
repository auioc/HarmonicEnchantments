package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public class HandinessEnchantment extends AbstractHEEnchantment implements IProjectileEnchantment.HurtLiving {

    public HandinessEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND, 2);
    }

    public int getMinCost(int lvl) {
        return 12 + (lvl - 1) * 20;
    }

    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 25;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.PUNCH_ARROWS;
    }

    @Override
    public float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 postion, float amount) {
        int amplifier = Math.min(lvl, this.getMaxLevel()) - 1;
        double duration = 6;
        if (lvl > this.getMaxLevel()) for (int k = 3, n = lvl + 1; k < n; k++) duration += (2 / (((double) k) - 2.0D));
        owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ((int) duration) * 20, amplifier));
        return amount;
    }

}
