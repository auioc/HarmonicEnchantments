/*
 * Copyright (C) 2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.phys.Vec3;

public interface HELocationBasedEffect extends EnchantmentLocationBasedEffect {

    void onChangedBlock(ServerLevel level, float input, EnchantedItemInUse item, Entity entity, Vec3 pos, boolean applyTransientEffects);

    void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 pos, float input);

    @Override
    default void onChangedBlock(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        onChangedBlock(level, (float) enchantmentLevel, item, entity, pos, applyTransientEffects);
    }

    @Override
    default void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 pos, int enchantmentLevel) {
        onDeactivated(item, entity, pos, (float) enchantmentLevel);
    }

    static void onChangedBlock(EnchantmentLocationBasedEffect effect, ServerLevel level, float input, EnchantedItemInUse item, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        if (effect instanceof HELocationBasedEffect e) {
            e.onChangedBlock(level, input, item, entity, pos, applyTransientEffects);
        } else {
            effect.onChangedBlock(level, (int) input, item, entity, pos, applyTransientEffects);
        }
    }

    static void onDeactivated(EnchantmentLocationBasedEffect effect, EnchantedItemInUse item, Entity entity, Vec3 pos, float input) {
        if (effect instanceof HELocationBasedEffect e) {
            e.onDeactivated(item, entity, pos, input);
        } else {
            effect.onDeactivated(item, entity, pos, (int) input);
        }
    }

}
