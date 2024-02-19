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

package org.auioc.mcmod.harmoniclib.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmoniclib.HarmonicLib;
import org.auioc.mcmod.harmoniclib.advancement.predicate.HEPerformancePredicateType;

public class HLRegistries {

    private static final ResourceKey<Registry<HEPerformancePredicateType>> EPPT_KEY = key("enchantment_performance_predicate_type");
    private static final DeferredRegister<HEPerformancePredicateType> EPPT = def(EPPT_KEY);
    public static final Registry<HEPerformancePredicateType> ENCHANTMENT_PERFORMANCE_PREDICATE_TYPE = EPPT.makeRegistry(builder -> { });

    // ============================================================================================================== //

    public static void init(IEventBus modEventBus) {
        EPPT.register(modEventBus);
    }

    private static <T> ResourceKey<Registry<T>> key(String id) {
        return ResourceKey.createRegistryKey(HarmonicEnchantments.id(id));
    }

    private static <T> DeferredRegister<T> def(ResourceKey<? extends Registry<T>> key) {
        return DeferredRegister.create(key, HarmonicLib.MOD_ID);
    }


}
