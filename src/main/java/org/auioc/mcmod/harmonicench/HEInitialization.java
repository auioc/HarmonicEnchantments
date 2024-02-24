/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import org.auioc.mcmod.harmonicench.advancement.HEEPerformancePredicates;
import org.auioc.mcmod.harmonicench.config.HECommonConfig;
import org.auioc.mcmod.harmonicench.effect.HEMobEffects;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

public final class HEInitialization {

    public static void init() {
        registerConfig();
        modSetup();
    }

    private static final IEventBus modEventBus = HarmonicEnchantments.getModEventBus();

    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HECommonConfig.CONFIG);
    }

    private static void modSetup() {
        HEEnchantments.ENCHANTMENTS.register(modEventBus);
        HEMobEffects.MOB_EFFECTS.register(modEventBus);
        HEEPerformancePredicates.EPPT.register(modEventBus);
    }

}
