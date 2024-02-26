/*
 * Copyright (C) 2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.data;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

public class HETags {

    public static final TagKey<Structure> DOWSING_LOCATABLE = tag(Registries.STRUCTURE, "dowsing_locatable");

    // ============================================================================================================== //

    private static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryKey, String patch) {
        // TODO arnicalib
        return TagKey.create(registryKey, HarmonicEnchantments.id(patch));
    }

}
