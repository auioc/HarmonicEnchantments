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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

/**
 * <b>强敌杀手 Bane of Champions</b>
 * <p>
 * 对一些强大的生物造成额外伤害。（生命值超过50点的敌对生物）。
 * <ul>
 *     <li>每一级增加2.5点额外伤害。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BaneOfChampionsEnchantment extends HLEnchantment implements ILivingEnchantment.Hurt {

    public BaneOfChampionsEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> !(o instanceof DamageEnchantment)
        );
    }

    // Ⅰ:  5 - 25
    // Ⅱ: 13 - 33
    // Ⅲ: 21 - 41
    // Ⅳ: 29 - 49
    // Ⅴ: 37 - 57
    @Override
    public int getMinCost(int lvl) {
        return lvl * 8 - 3;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem || super.canEnchant(itemStack);
    }

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (isSource && target.getHealth() > 50.0F) {
            return amount + ((float) lvl) * 2.5F;
        }
        return amount;
    }

}
