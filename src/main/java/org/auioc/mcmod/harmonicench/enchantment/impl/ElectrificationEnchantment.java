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
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import org.auioc.mcmod.arnicalib.game.enchantment.HLevelBasedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;

/**
 * <b>感电 Electrification</b>
 * <p>
 * 降雨或雷暴天气时，有概率产生闪电击中持有者并给予力量效果。
 * <ul>
 *     <li>降雨（雷暴）时，每 tick 有 0.2%（0.4%）概率产生闪电击中持有者。</li>
 *     <li>持有者被任何闪电击中后，获得力量效果，等级为 <code>⌊∑(n,k=1)[5/(2k+1)]⌋</code> 级，持续 <code>10×∑(n,k=1)(1/k)</code> 秒。</li>
 * </ul>
 */
public class ElectrificationEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.IMPALING, Enchantments.RIPTIDE
    );

    /**
     * Ⅰ:  1 - 21 <br>
     * Ⅱ:  9 - 29 <br>
     * Ⅲ: 17 - 37 <br>
     * Ⅳ: 25 - 45 <br>
     * Ⅴ: 33 - 53 <br>
     */
    private static final Cost COST = dynamicCost(1, 8, 21, 8);

    /**
     * <code>10 × ∑(lvl,k=1)(1/k)</code>
     */
    private static final LevelBasedValue STRENGTH_DURATION = new LevelBasedValue.Fraction(HEValueProviders.harmonic(), LevelBasedValue.constant(0.1F));
    /**
     * <code>⌊∑(lvl,k=1)[5/(2k+1)]⌋ - 1</code>
     */
    private static final LevelBasedValue STRENGTH_AMPLIFIER = HLevelBasedValue.sum(
        HEValueProviders.floor(HEValueProviders.harmonic(5F, LevelBasedValue.perLevel(3F, 2F))),
        LevelBasedValue.constant(-1F)
    );

    private static void summonLightning(Enchantment.Builder builder, float chance, boolean thundering) {
        builder.withEffect(
            EnchantmentEffectComponents.TICK,
            new SummonEntityEffect(HolderSet.direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()), false),
            AllOfCondition.allOf(
                LootItemRandomChanceCondition.randomChance(chance),
                WeatherCheck.weather().setRaining(true).setThundering(thundering),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().located(LocationPredicate.Builder.location().setCanSeeSky(true))
                )
            )
        );
    }

    private static final BuilderFunction BUILDER = define(
        ItemTags.TRIDENT_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        5,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> {
            summonLightning(builder, 0.004F, true);
            summonLightning(builder, 0.002F, false);
            builder.withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.VICTIM,
                EnchantmentTarget.VICTIM,
                new ApplyMobEffect(
                    HolderSet.direct(MobEffects.DAMAGE_BOOST),
                    STRENGTH_DURATION, STRENGTH_DURATION,
                    STRENGTH_AMPLIFIER, STRENGTH_AMPLIFIER
                ),
                DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
                    .source(EntityPredicate.Builder.entity().of(lookupEntity(ctx), EntityType.LIGHTNING_BOLT)))
            );
            return builder;
        }
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
