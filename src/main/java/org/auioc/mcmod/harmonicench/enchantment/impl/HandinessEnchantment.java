/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.harmonicench.enchantment.impl;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmoniclib.enchantment.api.AbstractHEEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IProjectileEnchantment;

public class HandinessEnchantment extends AbstractHEEnchantment implements IProjectileEnchantment.HurtLiving {

    public HandinessEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BOW,
            EquipmentSlot.MAINHAND,
            2,
            (o) -> o != Enchantments.PUNCH_ARROWS
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 12 + (lvl - 1) * 20;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 25;
    }

    @Override
    public float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 postion, float amount) {
        int amplifier = Math.min(lvl, this.getMaxLevel()) - 1;
        double duration = 6;
        if (lvl > this.getMaxLevel()) duration += MathUtil.sigma(lvl, 3, (double i) -> (2 / (i - 2.0D)));
        owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ((int) duration) * 20, amplifier));
        return amount;
    }

}
