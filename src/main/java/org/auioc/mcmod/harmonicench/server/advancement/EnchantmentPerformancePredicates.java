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

package org.auioc.mcmod.harmonicench.server.advancement;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.advancement.EPPredicateType;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.AimEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.CurseOfRebellingEnchantment;

public class EnchantmentPerformancePredicates {

    public static final ResourceKey<Registry<EPPredicateType>> KEY = ResourceKey.createRegistryKey(HarmonicEnchantments.id("epp"));

    private static final DeferredRegister<EPPredicateType> EPP = DeferredRegister.create(KEY, HarmonicEnchantments.MOD_ID);

    private static final Registry<EPPredicateType> REGISTRY = EPP.makeRegistry(builder -> { });

    public static final Codec<IEnchantmentPerformancePredicate> CODEC = ExtraCodecs.lazyInitializedCodec(() ->
        REGISTRY.byNameCodec().dispatch("performance", IEnchantmentPerformancePredicate::getType, EPPredicateType::codec)
    );

    public static void init(IEventBus modEventBus) {
        EPP.register(modEventBus);
    }

    // ============================================================================================================== //

    private static DeferredHolder<EPPredicateType, EPPredicateType> register(String id, Codec<? extends IEnchantmentPerformancePredicate> codec) {
        return EPP.register(id, () -> new EPPredicateType(codec));
    }

    public static DeferredHolder<EPPredicateType, EPPredicateType> AIM = register("aim", AimEnchantment.PerformancePredicate.CODEC);
    public static DeferredHolder<EPPredicateType, EPPredicateType> CURSE_OF_REBELLING = register("curse_of_rebelling", CurseOfRebellingEnchantment.PerformancePredicate.CODEC);


}
