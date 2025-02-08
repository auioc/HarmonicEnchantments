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

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.auioc.mcmod.arnicalib.game.critereon.HealthPredicate;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.effect.SiphoningEffect;

/**
 * <b>汲取 Siphoning</b>
 * <p>
 * 杀死生物后，根据其最大生命值恢复饥饿值和饱和度，优先回复饥饿值。
 * <ul>
 *     <li>恢复 <code>(x/15)∑(n,k=1)(1/k)</code> 点饥饿值或饱和度。（x：被击杀生物的最大生命值）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SiphoningEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.SWORDS, ItemTags.AXES
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.LOOTING);

    /**
     * Ⅰ: 15 - 61 <br>
     * Ⅱ: 24 - 71 <br>
     * Ⅲ: 33 - 81 <br>
     */
    private static final Cost COST = dynamicCost(15, 9, 61, 9);

    private static final HEEnchantedValue VALUE = HEEnchantedValue.product(
        HEEnchantedValue.fraction(HEEnchantedValue.identity(), HEEnchantedValue.constant(15)),
        HEEnchantedValue.harmonic(HEEnchantedValue.level(), 1.0F, HEEnchantedValue.identity()) // TODO reuse?
    );

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.SWORDS,
        EXCLUSIVE,
        Rarity.RARE,
        3,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
            new SiphoningEffect(VALUE),
            LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().subPredicate(HealthPredicate.current(MinMaxBounds.Doubles.atMost(0.0D)))
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
