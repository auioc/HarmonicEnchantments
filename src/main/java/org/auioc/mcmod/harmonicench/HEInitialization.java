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

package org.auioc.mcmod.harmonicench;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffects;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.HEValueProviders;
import org.auioc.mcmod.harmonicench.handler.HEEventHandler;
import org.auioc.mcmod.harmonicench.loot.HELootContextParamSets;

public final class HEInitialization {

    public static void init() {
        registerConfig();
        modSetup();
        forgeSetup();
    }

    private static final IEventBus modEventBus = HarmonicEnchantments.getModEventBus();
    private static final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

    public static void registerConfig() {
        //        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HECommonConfig.CONFIG);
    }

    private static void modSetup() {
        modEventBus.addListener(HEInitialization::commonSetup);
        HEValueProviders.TYPES.register(modEventBus);
        HEEnchantmentEffectComponents.TYPES.register(modEventBus);
        HEEnchantmentEffects.ENTITY_EFFECT_TYPES.register(modEventBus);
        HEEnchantmentEffects.LOCATION_BASED_EFFECT_TYPES.register(modEventBus);
        modEventBus.addListener(HEEnchantments::register);
        modEventBus.addListener(HEInitialization::register);
    }

    private static void forgeSetup() {
        forgeEventBus.register(HEEventHandler.class);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        HELootContextParamSets.init();
    }

    private static void register(RegisterEvent event) {
        if (event.getRegistryKey().compareTo(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE) == 0) {
            HEEnchantmentEffects.bootstrap();
        }
    }

}
