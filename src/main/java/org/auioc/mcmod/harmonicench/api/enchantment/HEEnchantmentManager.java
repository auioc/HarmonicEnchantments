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

package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

import javax.annotation.Nullable;
import java.util.HashMap;


public class HEEnchantmentManager {

    private static final HashMap<ResourceLocation, BooleanValue> ENABLED = new HashMap<>();

    @Nullable
    public static BooleanValue getConfigEnabled(ResourceLocation id) {
        return ENABLED.get(id);
    }

    public static void put(ResourceLocation id, BooleanValue configEnabled) {
        ENABLED.put(id, configEnabled);
    }

}
