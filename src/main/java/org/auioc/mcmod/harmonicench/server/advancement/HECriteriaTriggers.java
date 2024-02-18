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

package org.auioc.mcmod.harmonicench.server.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.server.advancement.criterion.EnchantmentPerformedTrigger;

import java.util.function.Supplier;

public final class HECriteriaTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, HarmonicEnchantments.MOD_ID);

    private static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String _id, Supplier<T> _sup) {
        return TRIGGERS.register(_id, _sup);
    }

    // ============================================================================================================== //

    public static final DeferredHolder<CriterionTrigger<?>, EnchantmentPerformedTrigger> ENCHANTMENT_PERFORMED = register("enchantment_performed", EnchantmentPerformedTrigger::new);

}
