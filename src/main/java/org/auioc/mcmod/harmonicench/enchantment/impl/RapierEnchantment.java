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
import net.neoforged.neoforge.common.ItemAbilities;
import org.auioc.mcmod.arnicalib.base.event.EventResult;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;

import java.util.Map;

/**
 * <b>迅捷之刃 Rapier</b>
 * <p>
 * 增加剑的攻击速度，但无法再使出横扫攻击。
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class RapierEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.SWEEPING_EDGE,
        HEEnchantments.LONG, HEEnchantments.BLUNT
    );

    /**
     * Ⅰ:  5 - 20 <br>
     * Ⅱ: 14 - 29 <br>
     * Ⅲ: 23 - 38 <br>
     */
    private static final Cost COST = dynamicCost(5, 9, 20, 9);

    /**
     * <code>∑(lvl,k=1)[1/(k+9)]</code>
     */
    private static final LevelBasedValue ATTACK_SPEED_BONUS = HEValueProviders.harmonic(
        LevelBasedValue.perLevel(10F, 1F)
    );

    private static final BuilderFunction BUILDER = define(
        ItemTags.SWORD_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.COMMON,
        3,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.ATTRIBUTES,
            new EnchantmentAttributeEffect(
                HarmonicEnchantments.id("enchantment.rapier"),
                Attributes.ATTACK_SPEED,
                ATTACK_SPEED_BONUS,
                AttributeModifier.Operation.ADD_VALUE
            )
        )
        .withSpecialEffect(
            HEEnchantmentEffectComponents.ITEM_ABILITIES.get(),
            Map.of(ItemAbilities.SWORD_SWEEP, EventResult.DENY)
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
