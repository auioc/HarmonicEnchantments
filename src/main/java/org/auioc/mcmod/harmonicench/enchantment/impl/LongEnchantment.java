/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.HELevelBasedValue;

/**
 * <b>延展之刃 Long</b>
 * <p>
 * 提高剑的攻击距离。
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class LongEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.SWEEPING_EDGE, HEEnchantments.RAPIER
    );


    /**
     * Ⅰ:  5 - 20 <br>
     * Ⅱ: 14 - 29 <br>
     * Ⅲ: 23 - 38 <br>
     */
    private static final Cost COST = dynamicCost(5, 9, 20, 9);

    /**
     * <code>∑(lvl,k=1)(3/4k)</code>
     */
    private static final LevelBasedValue ATTACK_SPEED_BONUS = HELevelBasedValue.harmonic(
        3F, LevelBasedValue.perLevel(4F)
    );

    private static final BuilderFunction BUILDER = define(
        ItemTags.SWORD_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        3,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.ATTRIBUTES,
            new EnchantmentAttributeEffect(
                HarmonicEnchantments.id("enchantment.long"),
                Attributes.ATTACK_SPEED,
                ATTACK_SPEED_BONUS,
                AttributeModifier.Operation.ADD_VALUE
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
