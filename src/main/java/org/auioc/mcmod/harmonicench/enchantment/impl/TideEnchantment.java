/*
 * Copyright (C) 2024 AUIOC.ORG
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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;

/**
 * <b>唤潮 Tide</b> TODO
 * <p>
 * 提高水中投掷速度，击中目标会使其窒息。
 * <ul>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.0
 */
public class TideEnchantment extends HLEnchantment {

    public TideEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.TRIDENT,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.LOYALTY && o != Enchantments.CHANNELING
                && o != HEEnchantments.ELECTRIFICATION.get()
        );
    }

    // Ⅰ: 12 - 50
    // Ⅱ: 19 - 50
    // Ⅲ: 26 - 50
    @Override
    public int getMinCost(int lvl) {
        return lvl * 7 + 5;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 50;
    }

}
