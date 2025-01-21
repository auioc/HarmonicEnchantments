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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public record ItemDamagedEffect(EnchantmentValueEffect value, EnchantmentEntityEffect effect) {

    public static final Codec<ItemDamagedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(o -> o.value),
        EnchantmentEntityEffect.CODEC.fieldOf("effect").forGetter(o -> o.effect)
    ).apply(instance, ItemDamagedEffect::new));


    public void apply(int lvl, EnchantedItemInUse item, LivingEntity owner, int damage) {
        damage = Math.round(value.process(lvl, owner.getRandom(), damage));
        if (damage > 0) {
            effect.apply((ServerLevel) owner.level(), damage, item, owner, owner.position());
        }
    }

}
