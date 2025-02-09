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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.enchantment.effects.MultiplyValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;
import org.auioc.mcmod.harmonicench.enchantment.effect.ChangeSpectralArrowDuration;
import org.auioc.mcmod.harmonicench.enchantment.effect.ModifyMobEffect;

/**
 * <b>效能 Efficacy</b>
 * <p>
 * 提高射出药箭和光灵箭施加状态效果的等级与持续时间。
 */
public class EfficacyEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems(
        (tag) -> tag.addTag(ItemTags.BOW_ENCHANTABLE).addTag(ItemTags.CROSSBOW_ENCHANTABLE)
    );

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MULTISHOT, Enchantments.PIERCING, Enchantments.POWER, Enchantments.FLAME
    );

    /**
     * Ⅰ:  1 - 50 <br>
     * Ⅱ: 11 - 50 <br>
     * Ⅲ: 21 - 50 <br>
     * Ⅳ: 31 - 50 <br>
     */
    private static final Cost COST = new Cost(Enchantment.dynamicCost(1, 10), Enchantment.constantCost(50));

    /**
     * <code>duration * [1+(n+1)*10%]</code>
     */
    private static final EnchantmentValueEffect DURATION_BONUS = new MultiplyValue(LevelBasedValue.perLevel(1.2F, 0.1F));

    /**
     * <code>amplifier + ∑(lvl,k=1)(1/k)</code>
     */
    private static final EnchantmentValueEffect AMPLIFIER_BONUS = new AddValue(HEValueProviders.harmonic());

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        EXCLUSIVE,
        Rarity.RARE,
        4,
        COST,
        1,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.PROJECTILE_SPAWNED,
            new ChangeSpectralArrowDuration(DURATION_BONUS)
        )
        .withEffect(
            HEEnchantmentEffectComponents.ARROW_POTION.get(),
            ModifyMobEffect.of(DURATION_BONUS, AMPLIFIER_BONUS)
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
