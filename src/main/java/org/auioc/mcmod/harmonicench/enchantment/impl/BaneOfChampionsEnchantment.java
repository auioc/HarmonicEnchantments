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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.auioc.mcmod.arnicalib.game.loot.predicate.EntityAttributeCondition;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>强敌杀手 Bane of Champions</b>
 * <p>
 * 对一些强大的生物造成额外伤害。（生命值超过50点的敌对生物）。
 * <ul>
 *     <li>每一级增加2.5点额外伤害。</li>
 * </ul>
 */
public class BaneOfChampionsEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems(
        (tag) -> tag.addTag(ItemTags.SWORD_ENCHANTABLE).addTag(ItemTags.AXES)
    );

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS,
        HEEnchantments.BLUNT
    );

    /**
     * Ⅰ:  5 - 25 <br>
     * Ⅱ: 13 - 33 <br>
     * Ⅲ: 21 - 41 <br>
     * Ⅳ: 29 - 49 <br>
     * Ⅴ: 37 - 57 <br>
     */
    private static final Cost COST = dynamicCost(5, 8, 25, 8);

    private static final LootItemCondition.Builder IS_CHAMPION = () -> new EntityAttributeCondition(
        Attributes.MAX_HEALTH,
        EntityAttributeCondition.ValueType.BASE,
        MinMaxBounds.Doubles.atLeast(50.0D),
        LootContext.EntityTarget.THIS
    );

    private static final EnchantmentValueEffect DAMAGE_BONUS = new AddValue(LevelBasedValue.perLevel(2.5F));

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.SWORD_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.UNCOMMON,
        5,
        COST,
        2,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(EnchantmentEffectComponents.DAMAGE, DAMAGE_BONUS, IS_CHAMPION)
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
