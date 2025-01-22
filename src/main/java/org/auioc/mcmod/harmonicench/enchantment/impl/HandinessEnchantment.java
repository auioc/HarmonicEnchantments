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
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import org.auioc.mcmod.arnicalib.game.enchantment.HLevelBasedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;

import java.util.List;

/**
 * <b>轻巧 Handiness</b>
 * <p>
 * 射出的箭命中实体后会短暂提高使用者移动速度。
 * <ul>
 *     <li>轻巧Ⅰ（Ⅱ）提供 6 秒速度Ⅰ（Ⅱ）。</li>
 *     <li>更高等级的附魔不提升效果等级，但持续时间增加 <code>∑(n,k=3)[2/(k-2)]</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class HandinessEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.PUNCH);

    /**
     * Ⅰ: 12 - 37 <br>
     * Ⅱ: 32 - 57 <br>
     */
    private static final Cost COST = dynamicCost(12, 20, 37, 20);

    /**
     * lookup <code>[6, 6, (6 + ∑(lvl,k=3)[2/(k-2)])]</code>
     */
    private static final LevelBasedValue SPEED_DURATION = LevelBasedValue.lookup(
        List.of(6F, 6F),
        HLevelBasedValue.sum(
            LevelBasedValue.constant(6F),
            HEValueProviders.harmonic(3, 2F, LevelBasedValue.perLevel(-1F, 1F))
        )
    );
    private static final LevelBasedValue SPEED_AMPLIFIER = LevelBasedValue.lookup(List.of(0F), LevelBasedValue.constant(1F));

    private static final BuilderFunction BUILDER = define(
        ItemTags.BOW_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        2,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(EnchantmentEffectComponents.POST_ATTACK,
            EnchantmentTarget.ATTACKER, EnchantmentTarget.ATTACKER, new ApplyMobEffect(
                HolderSet.direct(MobEffects.MOVEMENT_SPEED),
                SPEED_DURATION, SPEED_DURATION,
                SPEED_AMPLIFIER, SPEED_AMPLIFIER
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
