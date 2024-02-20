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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

/**
 * <b>汲取 Siphoning</b>
 * <p>
 * 杀死生物后，根据其最大生命值恢复饥饿值和饱和度，优先回复饥饿值。
 * <ul>
 *     <li>恢复 <code>(x/15)∑(n,k=1)(1/k)</code> 点饥饿值或饱和度。（x：被击杀生物的最大生命值）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SiphoningEnchantment extends HLEnchantment implements ILivingEnchantment.Death {

    public SiphoningEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.MOB_LOOTING
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
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem || super.canEnchant(itemStack);
    }

    @Override
    public void onLivingDeath(int lvl, ItemStack itemStack, LivingEntity target, DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return;

        double x = target.getMaxHealth();
        double r = MathUtil.sigma(lvl, 1, (double i) -> (x / 15.0D) * (1.0D / i));

        var food = player.getFoodData();
        if (food.needsFood()) {
            food.setFoodLevel(Math.min(food.getFoodLevel() + ((int) r), 20));
        } else {
            food.setSaturation(Math.min(food.getSaturationLevel() + ((float) r), food.getFoodLevel()));
        }
    }

}
