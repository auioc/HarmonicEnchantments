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

package org.auioc.mcmod.harmonicench.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.Vec3;

public record ChangeFrozenTicks(EnchantmentValueEffect value) implements EnchantmentEntityEffect {

    public static final MapCodec<ChangeFrozenTicks> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(o -> o.value)
    ).apply(instance, ChangeFrozenTicks::new));

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        entity.setTicksFrozen(Math.max(0, Math.round(value.process(lvl, entity.getRandom(), entity.getTicksFrozen()))));
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() { return CODEC; }

}
