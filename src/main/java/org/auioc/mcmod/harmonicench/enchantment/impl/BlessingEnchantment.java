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

import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>祝福 Blessing</b>
 * <p>
 * 根据物品的所有魔咒等级之和，提供魔法抗性。
 * <ul>
 *     <li>增加 <code>∑(N,i=1)(6/i)×∑(n,j=1)[1/(5j-4)]</code> 点魔法抗性。（N：该物品所有魔咒等级之和）</li>
 * </ul>
 */
public class BlessingEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.add(
        Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
        Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
        Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MENDING,
        HEEnchantments.FORGING, HEEnchantments.DINING
    );

    /**
     * Ⅰ: 1 - 51 <br>
     */
    private static final Cost COST = constantCost(1, 51);

    /**
     * <code>∑(totalEnchantmentLevel,k=1)(6/k)</code>
     */
    private static final HEEnchantedValue _FORMULA_1 = HEEnchantedValue.harmonic(HEEnchantedValue.totalLevel(), 6.0F, HEEnchantedValue.identity());
    /**
     * <code>(5 * k) + (-4)</code>
     */
    private static final HEEnchantedValue _FORMULA_2_1 = HEEnchantedValue.sum(HEEnchantedValue.linear(5.0F), HEEnchantedValue.constant(-4.0F));
    /**
     * <code>∑(lvl,k=1)[1/(5k-4)]</code>
     */
    private static final HEEnchantedValue _FORMULA_2 = HEEnchantedValue.harmonic(HEEnchantedValue.level(), 1.0F, _FORMULA_2_1);
    /**
     * <code>original + (f1 * f2)</code>
     */
    private static final HEEnchantedValue DAMAGE_PROTECTION = HEEnchantedValue.sum(
        HEEnchantedValue.identity(),
        HEEnchantedValue.product(_FORMULA_1, _FORMULA_2)
    );

    private static final TagPredicate<DamageType> IS_MAGIC_DAMAGE = TagPredicate.is(DamageTypeTags.WITCH_RESISTANT_TO); // TODO add a new tag?

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        EXCLUSIVE,
        Rarity.RARE,
        1,
        COST,
        2,
        EquipmentSlotGroup.ARMOR
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.DAMAGE_PROTECTION.get(),
            DAMAGE_PROTECTION,
            DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(IS_MAGIC_DAMAGE))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable().treasure();
    }

}
