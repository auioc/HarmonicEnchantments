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

package org.auioc.mcmod.harmonicench.damagesource;

import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public final class HEDamageTypes {

    public static final ResourceKey<DamageType> CURSE_OF_REBELLING = ResourceKey.create(Registries.DAMAGE_TYPE, HarmonicEnchantments.id("curse_of_rebelling"));

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(CURSE_OF_REBELLING, new DamageType("curseOfRebelling", DamageScaling.NEVER, 0.1F));
    }

}
