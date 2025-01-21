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
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public record ModifyMobEffect(Optional<MobEffectsPredicate> predicate, Optional<EnchantmentValueEffect> duration, Optional<EnchantmentValueEffect> amplifier) {

    public static Codec<ModifyMobEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MobEffectsPredicate.CODEC.optionalFieldOf("predicate").forGetter(o -> o.predicate),
        EnchantmentValueEffect.CODEC.optionalFieldOf("duration").forGetter(o -> o.duration),
        EnchantmentValueEffect.CODEC.optionalFieldOf("amplifier").forGetter(o -> o.amplifier)
    ).apply(instance, ModifyMobEffect::new));

    public MobEffectInstance apply(MobEffectInstance original, int lvl, Entity source) {
        if (predicate.isPresent() && !predicate.get().matches(Map.of(original.getEffect(), original))) {
            return original;
        }

        if (duration.isEmpty() && amplifier.isEmpty()) {
            return original;
        }

        var newDuration = duration
            .filter((_i) -> !original.getEffect().value().isInstantenous())
            .map(v -> v.process(lvl, source.getRandom(), original.getDuration()))
            .orElse((float) original.getDuration());
        var newAmplifier = amplifier
            .map(v -> v.process(lvl, source.getRandom(), original.getAmplifier()))
            .orElse((float) original.getAmplifier());
        return new MobEffectInstance(
            original.getEffect(), newDuration.intValue(), newAmplifier.intValue(),
            original.isAmbient(), original.isVisible(), original.showIcon(), original.hiddenEffect
        );
    }


    public static ModifyMobEffect duration(EnchantmentValueEffect value) {
        return new ModifyMobEffect(Optional.empty(), Optional.of(value), Optional.empty());
    }

    public static ModifyMobEffect duration(MobEffectsPredicate.Builder predicate, EnchantmentValueEffect value) {
        return new ModifyMobEffect(predicate.build(), Optional.of(value), Optional.empty());
    }

    public static ModifyMobEffect amplifier(EnchantmentValueEffect value) {
        return new ModifyMobEffect(Optional.empty(), Optional.empty(), Optional.of(value));
    }

    public static ModifyMobEffect amplifier(MobEffectsPredicate.Builder predicate, EnchantmentValueEffect value) {
        return new ModifyMobEffect(predicate.build(), Optional.empty(), Optional.of(value));
    }

    public static ModifyMobEffect of(EnchantmentValueEffect duration, EnchantmentValueEffect amplifier) {
        return new ModifyMobEffect(Optional.empty(), Optional.of(duration), Optional.of(amplifier));
    }

    public static ModifyMobEffect of(MobEffectsPredicate.Builder predicate, EnchantmentValueEffect duration, EnchantmentValueEffect amplifier) {
        return new ModifyMobEffect(predicate.build(), Optional.of(duration), Optional.of(amplifier));
    }

    // ============================================================================================================== //

    public record ForEntity(ModifyMobEffect value) implements EnchantmentEntityEffect {

        public static MapCodec<ForEntity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ModifyMobEffect.CODEC.fieldOf("value").forGetter(o -> o.value)
        ).apply(instance, ForEntity::new));

        @Override
        public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
            if (entity instanceof LivingEntity living) {
                var toAdd = new ArrayList<MobEffectInstance>();
                for (var inst : living.getActiveEffects()) {
                    var newInst = value.apply(inst, lvl, living);
                    if (newInst != inst) {
                        toAdd.add(newInst);
                    }
                }
                toAdd.forEach(inst -> living.removeEffect(inst.getEffect()));
                toAdd.forEach(inst -> {
                    if (inst.getDuration() > 0 && inst.getAmplifier() >= 0) {
                        living.addEffect(inst);
                    }
                });
            }
        }

        @Override
        public MapCodec<? extends EnchantmentEntityEffect> codec() { return CODEC; }

    }

    public static ForEntity forEntity(ModifyMobEffect value) { return new ForEntity(value); }

}
