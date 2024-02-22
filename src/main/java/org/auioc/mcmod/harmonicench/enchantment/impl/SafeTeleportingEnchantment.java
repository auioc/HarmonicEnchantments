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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;

/**
 * <b>传送保护 Safe Teleporting</b>
 * <p>
 * 免疫末影珍珠的传送伤害，并在穿越维度时提供防御。
 * <ul>
 *     <li>免疫末影珍珠的传送伤害。</li>
 *     <li>穿越维度时，获得抗性提升Ⅰ，持续 <code>∑(n,k=1)(20/k)</code>秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SafeTeleportingEnchantment extends HLEnchantment {

    public SafeTeleportingEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            EnchantmentCategory.ARMOR_FEET,
            EquipmentSlot.FEET,
            4,
            (o) -> o != Enchantments.FALL_PROTECTION
        );
    }

    // Ⅰ:  5 - 11
    // Ⅱ: 11 - 17
    // Ⅲ: 17 - 23
    // Ⅳ: 23 - 29
    @Override
    public int getMinCost(int lvl) {
        return lvl * 6 - 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 6;
    }

    public static void handleLivingTravelToDimension(int lvl, LivingEntity living) {
        double duration = MathUtil.sigma(lvl, 1, (double i) -> 20.0D / i);
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) (duration * 20)));
    }

}
