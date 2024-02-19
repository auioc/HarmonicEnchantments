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

package org.auioc.mcmod.harmonicench.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.damagesource.HEDamageTypes;
import org.auioc.mcmod.harmonicench.datagen.provider.HEAdvancementProvider;
import org.auioc.mcmod.harmonicench.datagen.provider.HEDamageTypeTagsProvider;

import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HEDataGenerators {

    private static final RegistrySetBuilder BUILTIN_ENTRIES_BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, HEDamageTypes::bootstrap);

    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        boolean run = event.includeServer();
        var registries = generator.addProvider(run, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), BUILTIN_ENTRIES_BUILDER, Set.of(HarmonicEnchantments.MOD_ID)))
            .getRegistryProvider();

        generator.addProvider(run, new HEAdvancementProvider(output, registries, fileHelper));
        generator.addProvider(run, new HEDamageTypeTagsProvider(output, registries, fileHelper));
    }

}
