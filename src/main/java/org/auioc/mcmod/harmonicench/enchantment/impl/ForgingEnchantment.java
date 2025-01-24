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

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.effect.HEAttributeEffect;

import java.util.List;

/**
 * <b>锻打 Forging</b>
 * <p>
 * 根据盔甲的总附魔数量，增加护甲值和盔甲韧性。
 * <ul>
 *     <li>增加 <code>x∑(n,k=1)(1/2k)</code> 点护甲值和盔甲韧性。（x：该物品魔咒数）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ForgingEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.add(
        Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
        Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
        Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
        Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MENDING,
        HEEnchantments.BLESSING, HEEnchantments.DINING
    );

    /**
     * Ⅰ: 1 - 41 <br>
     */
    private static final Cost COST = constantCost(1, 41);

    /**
     * <code>enchantmentCount × ∑(lvl,k=1)(1/2k)</code>
     */
    private static final HEEnchantedValue BONUS = HEEnchantedValue.product(
        HEEnchantedValue.countEnchantments(),
        HEEnchantedValue.harmonic(HEEnchantedValue.linear(2.0F))
    );

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        EXCLUSIVE,
        Rarity.RARE,
        1,
        COST,
        2,
        EquipmentSlotGroup.ARMOR
    ).andThen((key, ctx, builder) -> builder
        .withSpecialEffect(
            HEEnchantmentEffectComponents.ATTRIBUTES.get(),
            List.of(
                new HEAttributeEffect(
                    HEEnchantments.FORGING,
                    Attributes.ARMOR,
                    BONUS,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                new HEAttributeEffect(
                    HEEnchantments.FORGING,
                    Attributes.ARMOR_TOUGHNESS,
                    BONUS,
                    AttributeModifier.Operation.ADD_VALUE
                )
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
