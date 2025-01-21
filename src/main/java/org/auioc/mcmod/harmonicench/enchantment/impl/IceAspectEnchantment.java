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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.enchantment.effects.SetValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.auioc.mcmod.arnicalib.game.critereon.FrozenPredicate;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HELevelBasedValue;
import org.auioc.mcmod.harmonicench.enchantment.effect.ChangeFrozenTicks;

/**
 * <b>冰霜附加 Ice Aspect</b>
 * <p>
 * 被击中的实体会被冰冻，每一击命中会延长冰冻时间。
 * <ul>
 *     <li>TODO 对实体数据 {@link Entity#DATA_TICKS_FROZEN} 为 0 的生物攻击会增加该值 <code>∑(n,k=1)(200/k)</code>，后续每次攻击命中会增加 <code>∑(n,k=1)(100/k)</code>。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class IceAspectEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.FIRE_ASPECT);

    /**
     * Ⅰ: 10 - 60 <br>
     * Ⅱ: 30 - 80 <br>
     */
    private static final Cost COST = dynamicCost(10, 20, 60, 20);

   /* private static final MobEffectsPredicate.Builder IS_SLOWNESS = MobEffectsPredicate.Builder.effects().and(MobEffects.MOVEMENT_SLOWDOWN);
    private static final LootItemCondition.Builder HAS_SLOWNESS = LootItemEntityPropertyCondition.hasProperties(
        LootContext.EntityTarget.THIS,
        EntityPredicate.Builder.entity().effects(IS_SLOWNESS)
    );
    private static final LevelBasedValue SLOWNESS_AMPLIFIER = LevelBasedValue.lookup(List.of(0F), LevelBasedValue.constant(1F));
    // <code>∑(lvl,k=1)(5/k) - 3.5</code>
    private static final LevelBasedValue SLOWNESS_DURATION_BASE = HLevelBasedValue.sum(
        new HELevelBasedValue.SigmaSum(
            1,
            new LevelBasedValue.Fraction(
                LevelBasedValue.constant(5F),
                LevelBasedValue.perLevel(1F)
            )
        ),
        LevelBasedValue.constant(-3.5F)
    );
    // <code>∑(lvl,k=1)(2.5/k)</code>
    private static final LevelBasedValue SLOWNESS_DURATION_ADDITION = new HELevelBasedValue.SigmaSum(
        1,
        new LevelBasedValue.Fraction(
            LevelBasedValue.constant(2.5F),
            LevelBasedValue.perLevel(1F)
        )
    );*/

    private static final FrozenPredicate IS_FREEZING = FrozenPredicate.ticks(MinMaxBounds.Ints.atLeast(1));
    private static final FrozenPredicate IS_NOT_FREEZING = FrozenPredicate.ticks(MinMaxBounds.Ints.exactly(0));

    /**
     * set <code>∑(lvl,k=1)(200/k)</code>
     */
    private static final EnchantmentValueEffect FROZEN_TICKS_BASE = new SetValue(
        HELevelBasedValue.harmonic(200F, LevelBasedValue.perLevel(1F))
    );

    /**
     * add <code>∑(lvl,k=1)(100/k)</code>
     */
    private static final EnchantmentValueEffect FROZEN_TICKS_ADDITION = new AddValue(
        HELevelBasedValue.harmonic(100F, LevelBasedValue.perLevel(1F))
    );

    private static final BuilderFunction BUILDER = define(
        ItemTags.SWORD_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        2,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
            .withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
                new ChangeFrozenTicks(FROZEN_TICKS_ADDITION),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().subPredicate(IS_FREEZING)
                )
            ).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
                new ChangeFrozenTicks(FROZEN_TICKS_BASE),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().subPredicate(IS_NOT_FREEZING)
                )
            )
     /*   .withEffect(
            EnchantmentEffectComponents.POST_ATTACK,
            EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
            ModifyMobEffect.forEntity(
                ModifyMobEffect.duration(
                    IS_SLOWNESS, new AddValue(HEHelper.ticksToSeconds(SLOWNESS_DURATION_ADDITION))
                )
            ),
            HAS_SLOWNESS
        ).withEffect(
            EnchantmentEffectComponents.POST_ATTACK,
            EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
            new ApplyMobEffect(
                HolderSet.direct(MobEffects.MOVEMENT_SLOWDOWN),
                SLOWNESS_DURATION_BASE, SLOWNESS_DURATION_BASE,
                SLOWNESS_AMPLIFIER, SLOWNESS_AMPLIFIER
            ),
            InvertedLootItemCondition.invert(HAS_SLOWNESS)
        )*/
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
