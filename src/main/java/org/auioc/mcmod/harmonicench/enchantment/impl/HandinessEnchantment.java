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
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IProjectileEnchantment;

/**
 * <b>轻巧 Handiness</b>
 * <p>
 * 射出的箭命中实体后会短暂提高使用者移动速度。
 * <ul>
 *     <li>轻巧Ⅰ（Ⅱ）提供 6 秒速度Ⅰ（Ⅱ）。</li>
 *     <li>更高等级的附魔不提升效果等级，但持续时间增加 <code>∑(n,k=3)[2/(k-2)]</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class HandinessEnchantment extends HLEnchantment implements IProjectileEnchantment.HurtLiving {

    public HandinessEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BOW,
            EquipmentSlot.MAINHAND,
            2,
            (o) -> o != Enchantments.PUNCH_ARROWS
        );
    }

    // Ⅰ: 12 - 37
    // Ⅱ: 32 - 57
    @Override
    public int getMinCost(int lvl) {
        return lvl * 20 - 8;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 25;
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
