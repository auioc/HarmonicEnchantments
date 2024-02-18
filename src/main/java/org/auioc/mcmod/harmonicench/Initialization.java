/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.harmonicench.client.ClientInitialization;
import org.auioc.mcmod.harmonicench.common.config.HECommonConfig;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.event.HECommonEventHandler;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import org.auioc.mcmod.harmonicench.server.advancement.EnchantmentPerformancePredicates;
import org.auioc.mcmod.harmonicench.server.advancement.HECriteriaTriggers;
import org.auioc.mcmod.harmonicench.server.event.HEServerEventHandler;

public final class Initialization {

    public static void init() {
        registerConfig();
        modSetup();
        forgeSetup();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientInitialization.init();
        }
    }

    private static final IEventBus modEventBus = HarmonicEnchantments.getModEventBus();
    private static final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HECommonConfig.CONFIG);
    }

    private static void modSetup() {
        HEEnchantments.ENCHANTMENTS.register(modEventBus);
        HEMobEffects.MOB_EFFECTS.register(modEventBus);
        EnchantmentPerformancePredicates.init(modEventBus);
        HECriteriaTriggers.TRIGGERS.register(modEventBus);
    }

    private static void forgeSetup() {
        forgeEventBus.register(HECommonEventHandler.class);
        forgeEventBus.register(HEServerEventHandler.class);
    }

}
