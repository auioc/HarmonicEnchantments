/*
 * Copyright (C) 2024 AUIOC.ORG
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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;

/**
 * <b>收割 Harvest</b> TODO
 * <p>
 * 提高收割作物（以及生物）的速度。
 * <ul>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.0
 */
public class HarvestEnchantment extends HLEnchantment {

    public HarvestEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            HEnchantmentCategory.HOE,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.BLOCK_EFFICIENCY && o != HEEnchantments.PROFICIENCY.get()
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
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShovelItem || super.canEnchant(itemStack);
    }

}
