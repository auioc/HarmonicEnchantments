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

import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FreeRidingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Hurt {

    public FreeRidingEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            HEnchantmentCategory.FOOD_ON_A_STACK,
            EquipmentSlot.MAINHAND,
            (o) -> o != Enchantments.MENDING && o != Enchantments.UNBREAKING
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 35;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, RandomSource random, ServerPlayer player) {
        return 0;
    }

}
