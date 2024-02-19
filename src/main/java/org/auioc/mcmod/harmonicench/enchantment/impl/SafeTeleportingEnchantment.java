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
import org.auioc.mcmod.harmoniclib.enchantment.api.AbstractHEEnchantment;
import org.auioc.mcmod.harmoniclib.event.HLServerEventHandler;

/**
 * @see HLServerEventHandler#onEnderPearlTeleport
 * @see HLServerEventHandler#onEntityTravelToDimension
 */
public class SafeTeleportingEnchantment extends AbstractHEEnchantment {

    public SafeTeleportingEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            EnchantmentCategory.ARMOR_FEET,
            EquipmentSlot.FEET,
            4,
            (o) -> o != Enchantments.FALL_PROTECTION
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 5 + (lvl - 1) * 6;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 6;
    }

    public static void handleLivingTravelToDimension(int lvl, LivingEntity living) {
        double duration = MathUtil.sigma(lvl, 1, (double i) -> 20.0D / i);
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) (duration * 20)));
    }

}
