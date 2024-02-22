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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;

/**
 * <b>冰雪眷顾 Luck of the Snow</b>
 * <p>
 * 在寒冷和积雪生物群系里，提高钓上宝藏的概率，降低钓上鱼和垃圾的概率。
 * <ul>
 *     <li>在寒冷生物群系中，提供相当于同等级海之眷顾的效果。</li>
 *     <li>在积雪生物群系中，提供相当于两倍等级海之眷顾的效果。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class LuckOfTheSnowEnchantment extends HLEnchantment implements IItemEnchantment.FishingRod {

    public LuckOfTheSnowEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.FISHING_ROD,
            EquipmentSlot.values(),
            3,
            (o) -> o != Enchantments.FISHING_LUCK
        );
    }

    // Ⅰ: 15 - 61
    // Ⅱ: 24 - 71
    // Ⅲ: 33 - 81
    @Override
    public int getMinCost(int lvl) {
        return lvl * 9 + 6;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 45 + lvl;
    }

    @Override
    public Pair<Integer, Integer> preFishingRodCast(int lvl, ItemStack fishingRod, ServerPlayer player, int speedBonus, int luckBonus) {
        float temperature = player.level().getBiome(player.blockPosition()).value().getBaseTemperature();
        if (temperature <= 0.05F) {
            luckBonus += lvl * 2;
        } else if (temperature <= 0.3F) {
            luckBonus += lvl;
        }
        return Pair.of(speedBonus, luckBonus);
    }

}
