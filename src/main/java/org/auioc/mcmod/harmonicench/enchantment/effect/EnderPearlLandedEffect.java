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
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.arnicalib.base.event.CancelFlag;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

import java.util.function.Function;

public interface EnderPearlLandedEffect {

    ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends EnderPearlLandedEffect>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<EnderPearlLandedEffect> CODEC = REGISTRY.codec(ResourceLocation.CODEC).dispatch(EnderPearlLandedEffect::codec, Function.identity());

    static void bootstrap() {
        REGISTRY.put(HarmonicEnchantments.id("cancel_teleport"), CancelTeleport.CODEC);
        REGISTRY.put(HarmonicEnchantments.id("set_damage"), ChangeDamage.CODEC);
    }

    // ============================================================================================================== //

    void apply(ServerPlayer player, int lvl, EnchantedItemInUse item, ThrownEnderpearl pearl, MutableFloat damage, HitResult hit, CancelFlag cancelFlag);

    MapCodec<? extends EnderPearlLandedEffect> codec();

    // ============================================================================================================== //

    static CancelTeleport cancelTeleport() { return new CancelTeleport(); }

    class CancelTeleport implements EnderPearlLandedEffect {

        public static final MapCodec<CancelTeleport> CODEC = MapCodec.unit(CancelTeleport::new);

        @Override
        public void apply(ServerPlayer player, int lvl, EnchantedItemInUse item, ThrownEnderpearl pearl, MutableFloat damage, HitResult hit, CancelFlag cancelFlag) {
            cancelFlag.cancel();
        }

        @Override
        public MapCodec<CancelTeleport> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static ChangeDamage changeDamage(EnchantmentValueEffect value) { return new ChangeDamage(value); }

    record ChangeDamage(EnchantmentValueEffect value) implements EnderPearlLandedEffect {

        public static final MapCodec<ChangeDamage> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(o -> o.value)
        ).apply(instance, ChangeDamage::new));

        @Override
        public void apply(ServerPlayer player, int lvl, EnchantedItemInUse item, ThrownEnderpearl pearl, MutableFloat damage, HitResult hit, CancelFlag cancelFlag) {
            damage.setValue(value.process(lvl, player.getRandom(), damage.floatValue()));
        }

        @Override
        public MapCodec<? extends EnderPearlLandedEffect> codec() { return CODEC; }

    }

}
