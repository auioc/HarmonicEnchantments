/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IAttributeModifierEnchantment;

import java.util.Map;
import java.util.UUID;

/**
 * <b>延展之刃 Long</b>
 * <p>
 * 提高剑的攻击距离。
 * <ul>
 *     <li>增加攻击距离 <code>∑(n,k=1)(3/4k)</code>。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class LongEnchantment extends HLEnchantment implements IAttributeModifierEnchantment {

    private static final UUID ATTACK_RANGE_UUID = UUID.fromString("2A117CB2-C6AA-15F5-18EE-4B8C74CF8B9F");

    public LongEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.SWEEPING_EDGE && o != HEEnchantments.RAPIER.get()
        );
    }

    // Ⅰ:  5 - 20
    // Ⅱ: 14 - 29
    // Ⅲ: 23 - 38
    @Override
    public int getMinCost(int lvl) {
        return lvl * 9 - 4;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 15;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return Map.of(
            NeoForgeMod.ENTITY_REACH.value(),
            new AttributeModifier(
                ATTACK_RANGE_UUID, this.descriptionId,
                MathUtil.sigma(lvl, 1, (double i) -> 3.0D / (4.0D * i)), AttributeModifier.Operation.ADDITION
            )
        );
    }

}
