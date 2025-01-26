/*
 * Copyright (C) 2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.enchantment;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.arnicalib.base.event.EventResult;
import org.auioc.mcmod.arnicalib.game.codec.EnumCodec;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.effect.CriticalHitEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.DimensionTravelEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.EatingEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.EnderPearlLandedEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.HEAttributeEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.ItemDamagedEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.LootBonusCountEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.ModifyMobEffect;
import org.auioc.mcmod.harmonicench.loot.HELootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class HEEnchantmentEffectComponents {

    public static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, HarmonicEnchantments.MOD_ID);

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    // ============================================================================================================== //

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Map<ItemAbility, EventResult>>> ITEM_ABILITIES = register(
        "item_abilities", b -> b.persistent(Codec.unboundedMap(ItemAbility.CODEC, EnumCodec.byName(EventResult.class)))
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<HEAttributeEffect>>> ATTRIBUTES = register(
        "attributes", b -> b.persistent(HEAttributeEffect.CODEC.codec().listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LootBonusCountEffect>>>> LOOT_BONUS_COUNT = register(
        "loot_bonus_count", b -> b.persistent(ConditionalEffect.codec(LootBonusCountEffect.CODEC, LootContextParamSets.ALL_PARAMS).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<ItemDamagedEffect>>>> ITEM_DAMAGED = register(
        "item_damaged", b -> b.persistent(ConditionalEffect.codec(ItemDamagedEffect.CODEC, HELootContextParamSets.ENCHANTED_ITEM_WITH_ENTITY).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnderPearlLandedEffect>>>> ENDER_PEARL_LANDED = register(
        "ender_pearl_landed", b -> b.persistent(ConditionalEffect.codec(EnderPearlLandedEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<DimensionTravelEffect>>>> DIMENSION_TRAVEL = register(
        "dimension_travel", b -> b.persistent(ConditionalEffect.codec(DimensionTravelEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<ModifyMobEffect>>>> ARROW_POTION = register(
        "arrow_potion", b -> b.persistent(ConditionalEffect.codec(ModifyMobEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedConditionalEffect<CriticalHitEffect>>>> CRITICAL_HIT = register(
        "critical_hit", b -> b.persistent(TargetedConditionalEffect.codec(CriticalHitEffect.CODEC, HELootContextParamSets.ENCHANTED_DIRECT_ATTACK).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>> BLOCK_DESTROYED = register(
        "block_destroyed", b -> b.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, HELootContextParamSets.ENCHANTED_BLOCK_DESTROYED).listOf())
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EatingEffect>>>> EATING = register(
        "eating", b -> b.persistent(ConditionalEffect.codec(EatingEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf())
    );

}
