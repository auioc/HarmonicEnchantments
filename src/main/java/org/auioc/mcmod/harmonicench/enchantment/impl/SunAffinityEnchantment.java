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

import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import org.auioc.mcmod.arnicalib.base.event.EventResult;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;

import java.util.List;

/**
 * <b>领航 Sun Affinity</b>
 * <p>
 * 鞘翅滑翔更加轻盈，但只能在晴朗的白天使用。
 * <ul>
 *     <li>使用鞘翅时，降低玩家 20%/40%/60% 重力影响。（更高等级维持 60%）</li>
 *     <li>鞘翅只能在 0~12000 刻，并且天气为晴天时使用。（在末地和下界将一直无法使用）</li>
 * </ul>
 */
public class SunAffinityEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems(
        (tag) -> tag.add(Items.ELYTRA)
    );

    /**
     * Ⅰ: 10 - 40 <br>
     * Ⅱ: 20 - 50 <br>
     * Ⅲ: 30 - 60 <br>
     */
    private static final Cost COST = dynamicCost(10, 10, 40, 10);

    private static final LootItemCondition.Builder IS_SUNNY = WeatherCheck.weather().setRaining(false).setThundering(false);
    private static final LootItemCondition.Builder IS_DAYTIME = new TimeCheck.Builder(IntRange.range(0, 12000)).setPeriod(24000L);
    private static final LocationPredicate.Builder IS_OVERWORLD = LocationPredicate.Builder.inDimension(Level.OVERWORLD);

    private static final LevelBasedValue GRAVITY_MULTIPLIER = LevelBasedValue.lookup(List.of(0.8F, 0.6F), LevelBasedValue.constant(0.4F));

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        Rarity.RARE,
        3,
        COST,
        8,
        EquipmentSlotGroup.CHEST
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.GLIDE.get(),
            EventResult.DENY,
            InvertedLootItemCondition.invert(AllOfCondition.allOf(
                IS_SUNNY,
                IS_DAYTIME,
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().located(IS_OVERWORLD)
                )
            ))
        )
        .withEffect(
            EnchantmentEffectComponents.LOCATION_CHANGED,
            new EnchantmentAttributeEffect(
                HarmonicEnchantments.id("enchantment.sun_affinity"),
                Attributes.GRAVITY,
                GRAVITY_MULTIPLIER,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().flags(
                    EntityFlagsPredicate.Builder.flags().setIsFlying(true)
                )
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS).tradeable();
    }

}
