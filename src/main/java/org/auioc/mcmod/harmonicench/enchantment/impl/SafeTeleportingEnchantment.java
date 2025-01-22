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

import net.minecraft.core.HolderSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.item.enchantment.effects.SetValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;
import org.auioc.mcmod.harmonicench.enchantment.effect.DimensionTravelEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.EnderPearlLandedEffect;

/**
 * <b>传送保护 Safe Teleporting</b>
 * <p>
 * 免疫末影珍珠的传送伤害，并在穿越维度时提供防御。
 * <ul>
 *     <li>免疫末影珍珠的传送伤害。</li>
 *     <li>穿越维度时，获得抗性提升Ⅰ，持续 <code>∑(n,k=1)(20/k)</code>秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SafeTeleportingEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.FEATHER_FALLING);

    /**
     * Ⅰ:  5 - 20 <br>
     * Ⅱ: 14 - 29 <br>
     * Ⅲ: 23 - 38 <br>
     */
    private static final Cost COST = dynamicCost(5, 6, 11, 6);

    /**
     * <code>∑(lvl,k=1)(20/k)</code>
     */
    private static final LevelBasedValue RESISTANCE_DURATION = HEValueProviders.harmonic(
        20F, LevelBasedValue.perLevel(1F)
    );

    private static final BuilderFunction BUILDER = define(
        ItemTags.FOOT_ARMOR_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.UNCOMMON,
        4,
        COST,
        4,
        EquipmentSlotGroup.FEET
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.ENDER_PEARL_LANDED.get(),
            EnderPearlLandedEffect.changeDamage(new SetValue(LevelBasedValue.constant(0.0F)))
        ).withEffect(
            HEEnchantmentEffectComponents.DIMENSION_TRAVEL.get(),
            DimensionTravelEffect.entityEffect(new ApplyMobEffect(
                HolderSet.direct(MobEffects.DAMAGE_RESISTANCE),
                RESISTANCE_DURATION, RESISTANCE_DURATION,
                LevelBasedValue.constant(0.0F), LevelBasedValue.constant(0.0F)
            ))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
