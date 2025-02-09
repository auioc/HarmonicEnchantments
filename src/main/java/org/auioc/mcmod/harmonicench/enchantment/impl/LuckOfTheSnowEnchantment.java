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

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.auioc.mcmod.arnicalib.game.critereon.BiomePredicate;
import org.auioc.mcmod.arnicalib.game.loot.predicate.ExtraLocationCheck;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;

/**
 * <b>冰雪眷顾 Luck of the Snow</b>
 * <p>
 * 在寒冷和积雪生物群系里，提高钓上宝藏的概率，降低钓上鱼和垃圾的概率。
 * <ul>
 *     <li>在寒冷生物群系中，提供相当于同等级海之眷顾的效果。</li>
 *     <li>在积雪生物群系中，提供相当于两倍等级海之眷顾的效果。</li>
 * </ul>
 */
public class LuckOfTheSnowEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.LUCK_OF_THE_SEA);

    /**
     * Ⅰ: 15 - 61 <br>
     * Ⅱ: 24 - 71 <br>
     * Ⅲ: 33 - 81 <br>
     */
    private static final Cost COST = dynamicCost(15, 9, 61, 10);

    private static final MinMaxBounds.Doubles IS_COLD = MinMaxBounds.Doubles.between(0.05D, 0.35D);
    private static final EnchantmentValueEffect COLD_BONUS = new AddValue(LevelBasedValue.perLevel(1.0F));

    private static final MinMaxBounds.Doubles IS_SNOWY = MinMaxBounds.Doubles.atMost(0.05D);
    private static final EnchantmentValueEffect SNOWY_BONUS = new AddValue(LevelBasedValue.perLevel(2.0F));

    private static final BuilderFunction BUILDER = define(
        ItemTags.FISHING_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        3,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.FISHING_LUCK_BONUS, SNOWY_BONUS,
            ExtraLocationCheck.checkBiome(BiomePredicate.withClimate(b -> b.temperature(IS_SNOWY)))
        ).withEffect(
            EnchantmentEffectComponents.FISHING_LUCK_BONUS, COLD_BONUS,
            ExtraLocationCheck.checkBiome(BiomePredicate.withClimate(b -> b.temperature(IS_COLD)))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
