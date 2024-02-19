/*
 * Copyright (C) 2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmonicench.advancement;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.impl.AimEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.CurseOfRebellingEnchantment;
import org.auioc.mcmod.harmoniclib.advancement.predicate.HEPerformancePredicateType;
import org.auioc.mcmod.harmoniclib.advancement.predicate.IHEPerformancePredicate;
import org.auioc.mcmod.harmoniclib.core.HLRegistries;

public class HEEPerformancePredicates {

    public static final DeferredRegister<HEPerformancePredicateType> EPPT = DeferredRegister.create(HLRegistries.ENCHANTMENT_PERFORMANCE_PREDICATE_TYPE, HarmonicEnchantments.MOD_ID);

    // ============================================================================================================== //

    private static DeferredHolder<HEPerformancePredicateType, HEPerformancePredicateType> register(String id, Codec<? extends IHEPerformancePredicate> codec) {
        return EPPT.register(id, () -> new HEPerformancePredicateType(codec));
    }

    public static DeferredHolder<HEPerformancePredicateType, HEPerformancePredicateType> AIM = register("aim", AimEnchantment.PerformancePredicate.CODEC);
    public static DeferredHolder<HEPerformancePredicateType, HEPerformancePredicateType> CURSE_OF_REBELLING = register("curse_of_rebelling", CurseOfRebellingEnchantment.PerformancePredicate.CODEC);


}
