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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;

public record SiphoningEffect(HEEnchantedValue value) implements EnchantmentEntityEffect {

    public static final MapCodec<SiphoningEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        HEEnchantedValue.CODEC.fieldOf("value").forGetter(o -> o.value)
    ).apply(instance, SiphoningEffect::new));

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (item.owner() instanceof ServerPlayer player && entity instanceof LivingEntity living && player != living) {
            float maxHealth = living.getMaxHealth();
            float r = value.calculate(maxHealth, lvl, item);
            var food = player.getFoodData();
            if (food.needsFood()) {
                food.setFoodLevel(Math.min(food.getFoodLevel() + Math.round(r), 20));
            } else {
                food.setSaturation(Math.min(food.getSaturationLevel() + r, food.getFoodLevel()));
            }
        }
    }

    @Override
    public MapCodec<SiphoningEffect> codec() { return CODEC; }

}
