/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * HarmonicEnchantments is free software: you can redistribute it and/or modify it under
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

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IProjectileEnchantment;
import org.auioc.mcmod.harmoniclib.mixinapi.IMixinAbstractArrow;

/**
 * <b>狙击 Sniping</b>
 * <p>
 * 更容易命中，对远距离目标造成的伤害更高。
 * <ul>
 *     <li>对16格以外的实体造成伤害增加 <code>[(x-16)/32]∑(n,k=1)(1/k)</code> 倍。（对近处不会降低伤害）</li>
 *     <li>箭矢降低 15%/30%/45% 所受重力影响。（更高等级维持45%）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SnipingEnchantment extends HLEnchantment implements IProjectileEnchantment.HurtLiving, IProjectileEnchantment.AbstractArrow {

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

    // Ⅰ: 15 - 50
    // Ⅱ: 24 - 50
    // Ⅲ: 33 - 50
    @Override
    public int getMinCost(int lvl) {
        return lvl * 20 - 8;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 50;
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
