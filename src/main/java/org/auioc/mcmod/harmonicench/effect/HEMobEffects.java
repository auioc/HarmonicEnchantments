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

package org.auioc.mcmod.harmonicench.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.effect.impl.ConfusionMobEffect;
import org.auioc.mcmod.harmonicench.effect.impl.WeightlessnessMobEffect;

import java.util.function.Supplier;

public final class HEMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HarmonicEnchantments.MOD_ID);

    private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String id, Supplier<T> sup) {
        return MOB_EFFECTS.register(id, sup);
    }

    public static final DeferredHolder<MobEffect, WeightlessnessMobEffect> WEIGHTLESSNESS = register("weightlessness", WeightlessnessMobEffect::new);
    public static final DeferredHolder<MobEffect, ConfusionMobEffect> CONFUSION = register("confusion", ConfusionMobEffect::new);

}
