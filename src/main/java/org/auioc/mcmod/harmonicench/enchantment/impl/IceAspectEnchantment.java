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

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

/**
 * <b>冰霜附加 Ice Aspect</b>
 * <p>
 * 被击中的实体会被冰冻，每一击命中会延长冰冻时间。
 * <ul>
 *     <li>对实体数据 {@link Entity#DATA_TICKS_FROZEN} 为 0 的生物攻击会增加该值 <code>∑(n,k=1)(200/k)</code>，后续每次攻击命中会增加 <code>∑(n,k=1)(100/k)</code>。</li>
 *     <li>TODO 攻击同时会给予目标缓慢Ⅰ/Ⅱ（更高等级魔咒维持缓慢Ⅱ），初次命中持续时间 <code>[∑(n,k=1)(5/k)]-3.5</code> 秒，之后每次命中增加 <code>∑(n,k=1)(2.5/k)</code>。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class IceAspectEnchantment extends HLEnchantment implements ILivingEnchantment.Hurt {

    public IceAspectEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            2,
            (o) -> o != Enchantments.FIRE_ASPECT
        );
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
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (isSource && target.canFreeze()) {
            int ticksFrozen = target.getTicksFrozen();

            double f = (ticksFrozen == 0) ? 200.0D : 100.0D;
            double r = MathUtil.sigma(lvl, 1, (double i) -> f / i);
            target.setTicksFrozen(ticksFrozen + ((int) r));

            double t = (ticksFrozen == 0)
                       ? MathUtil.sigma(lvl, 1, (double i) -> 5.0D / i) - 3.5D
                       : MathUtil.sigma(lvl, 1, (double i) -> 2.5D / i);
            var effect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (t * 20.0D), Math.max(lvl, 2) - 1);
            target.addEffect(effect, source.getEntity());
        }
        return amount;
    }

}
