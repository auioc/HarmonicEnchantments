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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.AbstractHEEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;

public class BlessingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Protection {

    private static final EnchantmentCategory BLESSABLE_ARMOR = EnchantmentCategory.create(
        "BLESSABLE_ARMOR",
        (item) -> {
            if (item instanceof ArmorItem arrow && arrow.getMaterial() instanceof ArmorMaterials material) {
                switch (material) {
                    case LEATHER:
                    case GOLD:
                    case NETHERITE: {
                        return true;
                    }
                    default: {
                        break;
                    }
                }
            }
            return false;
        }
    );

    public BlessingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            BLESSABLE_ARMOR,
            new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET },
            (o) -> o != Enchantments.MENDING && o != HEEnchantments.FORGING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 51;
    }

    @Override
    public int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source) {
        if (source.is(DamageTypes.MAGIC)) {
            int N = EnchantmentHelper.getEnchantments(itemStack).values().stream().mapToInt((i) -> i).sum();
            double l = MathUtil.sigma(N, 1, (double i) -> 6.0D / i);
            double r = MathUtil.sigma(lvl, 1, (double j) -> 1.0D / (5.0D * j - 4.0D));
            return (int) (l * r);
        }
        return 0;
    }

}
