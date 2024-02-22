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
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IPlayerEnchantment;

/**
 * <b>饱食修补 Dining</b>
 * <p>
 * 食用食物后，将玩家增加的饥饿值转化为该物品的耐久度。
 * <ul>
 *     <li>每 1 点饥饿值增加 10 点耐久度。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class DiningEnchantment extends HLEnchantment implements IPlayerEnchantment.Eat {

    public DiningEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BREAKABLE,
            EquipmentSlot.values(),
            (o) -> o != Enchantments.MENDING
                && o != Enchantments.INFINITY_ARROWS
                && o != HEEnchantments.BLESSING.get()
                && o != HEEnchantments.FORGING.get()
                && o != HEEnchantments.FREE_RIDING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 25;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public Pair<Integer, Float> onPlayerEat(int lvl, ItemStack itemStack, EquipmentSlot slot, ServerPlayer player, ItemStack foodItemStack, int nutrition, float saturationModifier) {
        if (itemStack.isDamaged() && nutrition > 0) {
            int damage = itemStack.getDamageValue();
            int c = (int) Math.ceil(damage / 10.0D);
            if (nutrition >= c) {
                saturationModifier *= ((float) c) / ((float) nutrition);
                nutrition -= c;
                damage = 0;
            } else {
                damage -= nutrition * 10;
                saturationModifier = 0.0F;
                nutrition = 0;
            }
            itemStack.setDamageValue(damage);
        }
        return Pair.of(nutrition, saturationModifier);
    }

}
