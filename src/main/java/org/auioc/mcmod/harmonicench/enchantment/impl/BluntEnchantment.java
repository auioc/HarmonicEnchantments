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


import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.enchantment.effects.SetValue;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.Tags;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;
import org.auioc.mcmod.harmonicench.enchantment.effect.CriticalHitEffect;

/**
 * <b>钝重Blunt</b>
 * <p>
 * 降低攻击速度，大幅提高暴击伤害（跳劈）。
 * <ul>
 *     <li>降低 25% 攻击速度。</li>
 *     <li>暴击将造成 <code>0.5×(n+3)×100% </code>伤害。</li>
 *     <li>用红砖或下界砖暴击命中玩家，将给予混乱效果，持续时间 <code>∑(n,k=1)(5/k)</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BluntEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.AXES, ItemTags.SWORD_ENCHANTABLE, Tags.Items.BRICKS)
    );

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.EFFICIENCY,
        Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS,
        HEEnchantments.BANE_OF_CHAMPIONS, HEEnchantments.RAPIER
    );

    /**
     * Ⅰ:  5 - 25 <br>
     * Ⅱ: 13 - 33 <br>
     * Ⅲ: 21 - 41 <br>
     * Ⅳ: 29 - 49 <br>
     * Ⅴ: 37 - 57 <br>
     */
    private static final Cost COST = dynamicCost(5, 8, 25, 8);

    private static final LevelBasedValue ATTACK_SPEED_BONUS = LevelBasedValue.constant(-0.25F);

    /**
     * <code>0.5×(lvl+3)×100%</code>
     */
    private static final EnchantmentValueEffect CRITICAL_HIT_BONUS = new SetValue(LevelBasedValue.perLevel(2.0F, 0.5F));

    /**
     * <code>∑(lvl,k=1)(5/k)</code>
     */
    private static final LevelBasedValue EFFECT_DURATION = HEValueProviders.harmonic(
        5F, LevelBasedValue.perLevel(1F)
    );
    private static final LevelBasedValue EFFECT_AMPLIFIER = LevelBasedValue.constant(0F);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.AXES,
        EXCLUSIVE,
        Rarity.UNCOMMON,
        5,
        COST,
        2,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.ATTRIBUTES,
            new EnchantmentAttributeEffect(
                HarmonicEnchantments.id("enchantment.blunt"),
                Attributes.ATTACK_SPEED,
                ATTACK_SPEED_BONUS,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
        ).withEffect(
            HEEnchantmentEffectComponents.CRITICAL_HIT.get(),
            EnchantmentTarget.ATTACKER,
            EnchantmentTarget.VICTIM,
            CriticalHitEffect.changeMultiplier(CRITICAL_HIT_BONUS)
        ).withEffect(
            HEEnchantmentEffectComponents.CRITICAL_HIT.get(),
            EnchantmentTarget.ATTACKER,
            EnchantmentTarget.VICTIM,
            CriticalHitEffect.entityEffect(new ApplyMobEffect(
                HolderSet.direct(MobEffects.BLINDNESS), // TODO 混乱效果
                EFFECT_DURATION, EFFECT_DURATION,
                EFFECT_AMPLIFIER, EFFECT_AMPLIFIER
            )),
            MatchTool.toolMatches(ItemPredicate.Builder.item().of(lookupItem(ctx), Tags.Items.BRICKS))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
