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

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.effect.ChangeFrozenTicks;
import org.auioc.mcmod.harmonicench.enchantment.effect.ChangeSpectralArrowDuration;
import org.auioc.mcmod.harmonicench.enchantment.effect.CriticalHitEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.DimensionTravelEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.EnderPearlLandedEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.HEAttributeEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.ModifyMobEffect;

public class HEEnchantmentEffects {


    public static void bootstrap() {
        DimensionTravelEffect.bootstrap();
        EnderPearlLandedEffect.bootstrap();
        CriticalHitEffect.bootstrap();
    }

    // ============================================================================================================== //

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_EFFECT_TYPES =
        DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, HarmonicEnchantments.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<ChangeSpectralArrowDuration>> CHANGE_SPECTRAL_ARROW_DURATION =
        ENTITY_EFFECT_TYPES.register("change_spectral_arrow_duration", () -> ChangeSpectralArrowDuration.CODEC);

    public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<ModifyMobEffect.ForEntity>> MODIFY_MOB_EFFECT =
        ENTITY_EFFECT_TYPES.register("modify_mob_effect", () -> ModifyMobEffect.ForEntity.CODEC);

    public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<ChangeFrozenTicks>> CHANGE_FROZEN_TICKS =
        ENTITY_EFFECT_TYPES.register("change_frozen_ticks", () -> ChangeFrozenTicks.CODEC);

    // ============================================================================================================== //

    public static final DeferredRegister<MapCodec<? extends EnchantmentLocationBasedEffect>> LOCATION_BASED_EFFECT_TYPES =
        DeferredRegister.create(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, HarmonicEnchantments.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends EnchantmentLocationBasedEffect>, MapCodec<HEAttributeEffect>> ATTRIBUTE =
        LOCATION_BASED_EFFECT_TYPES.register("attribute", () -> HEAttributeEffect.CODEC);

}
