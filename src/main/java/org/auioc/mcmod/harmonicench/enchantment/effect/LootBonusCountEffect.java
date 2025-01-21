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
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public record LootBonusCountEffect(HolderSet<Enchantment> enchantment, EnchantmentValueEffect value) {

    public static Codec<LootBonusCountEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(o -> o.enchantment),
        EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(o -> o.value)
    ).apply(instance, LootBonusCountEffect::new));

}
