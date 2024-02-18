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

package org.auioc.mcmod.harmonicench.common.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.auioc.mcmod.harmonicench.api.enchantment.HEEnchantmentManager;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;

import java.util.Comparator;

public class HECommonConfig {

    public static final ModConfigSpec CONFIG;

    static {
        final ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.push("enabled");
        HEEnchantments.ENCHANTMENTS.getEntries()
            .stream()
            .map(DeferredHolder::getId)
            .sorted(Comparator.comparing(ResourceLocation::getPath))
            .forEach((e) -> HEEnchantmentManager.put(e, b.define(e.getPath(), true)));
        b.pop();

        CONFIG = b.build();
    }

}
