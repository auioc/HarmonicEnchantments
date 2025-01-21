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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.Level;
import org.auioc.mcmod.arnicalib.base.event.CancelFlag;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

import java.util.function.Function;

public interface DimensionTravelEffect {

    ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends DimensionTravelEffect>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<DimensionTravelEffect> CODEC = REGISTRY.codec(ResourceLocation.CODEC).dispatch(DimensionTravelEffect::codec, Function.identity());

    static void bootstrap() {
        REGISTRY.put(HarmonicEnchantments.id("cancel"), Cancel.CODEC);
        REGISTRY.put(HarmonicEnchantments.id("entity_effect"), EntityEffect.CODEC);
    }

    // ============================================================================================================== //

    void apply(LivingEntity living, int lvl, EnchantedItemInUse item, ResourceKey<Level> destinationDimension, CancelFlag cancelFlag);

    MapCodec<? extends DimensionTravelEffect> codec();

    // ============================================================================================================== //

    static Cancel cancel() { return new Cancel(); }

    class Cancel implements DimensionTravelEffect {

        public static final MapCodec<Cancel> CODEC = MapCodec.unit(Cancel::new);

        @Override
        public void apply(LivingEntity living, int lvl, EnchantedItemInUse item, ResourceKey<Level> destinationDimension, CancelFlag cancelFlag) {
            cancelFlag.cancel();
        }

        @Override
        public MapCodec<Cancel> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static EntityEffect entityEffect(EnchantmentEntityEffect effect) { return new EntityEffect(effect); }

    record EntityEffect(EnchantmentEntityEffect effect) implements DimensionTravelEffect {

        public static final MapCodec<EntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentEntityEffect.CODEC.fieldOf("effect").forGetter(o -> o.effect)
        ).apply(instance, EntityEffect::new));

        @Override
        public void apply(LivingEntity living, int lvl, EnchantedItemInUse item, ResourceKey<Level> destinationDimension, CancelFlag cancelFlag) {
            effect.apply((ServerLevel) living.level(), lvl, item, living, living.position());
        }

        @Override
        public MapCodec<EntityEffect> codec() { return CODEC; }

    }

}
