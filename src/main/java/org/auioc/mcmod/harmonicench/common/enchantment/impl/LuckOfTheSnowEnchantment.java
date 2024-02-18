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

package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class LuckOfTheSnowEnchantment extends AbstractHEEnchantment implements IItemEnchantment.FishingRod {

    public LuckOfTheSnowEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.FISHING_ROD,
            EquipmentSlot.values(),
            3,
            (o) -> o != Enchantments.FISHING_LUCK
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 15 + (lvl - 1) * 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public Pair<Integer, Integer> preFishingRodCast(int lvl, ItemStack fishingRod, ServerPlayer player, int speedBonus, int luckBonus) {
        float temperature = player.level().getBiome(player.blockPosition()).value().getBaseTemperature();
        if (temperature <= 0.0F) {
            luckBonus += lvl * 2;
        } else if (temperature <= 0.2F) {
            luckBonus += lvl;
        }
        return Pair.of(speedBonus, luckBonus);
    }

}
