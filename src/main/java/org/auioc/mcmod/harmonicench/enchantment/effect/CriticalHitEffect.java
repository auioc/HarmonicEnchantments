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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.arnicalib.base.event.CancelFlag;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

import java.util.List;
import java.util.function.Function;

public interface CriticalHitEffect {

    ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends CriticalHitEffect>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<CriticalHitEffect> CODEC = REGISTRY.codec(ResourceLocation.CODEC).dispatch(CriticalHitEffect::codec, Function.identity());

    static void bootstrap() {
        REGISTRY.put(HarmonicEnchantments.id("cancel"), Cancel.CODEC);
        REGISTRY.put(HarmonicEnchantments.id("change_multiplier"), ChangeMultiplier.CODEC);
        REGISTRY.put(HarmonicEnchantments.id("entity_effect"), EntityEffect.CODEC);
    }

    // ============================================================================================================== //

    static void apply(
        List<TargetedConditionalEffect<CriticalHitEffect>> effects, LootContext context, boolean isSource,
        int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target,
        MutableFloat damageMultiplier, CancelFlag cancelFlag
    ) {
        for (var effect : effects) {
            if (effect.matches(context)) {
                effect.effect().apply(isSource, effect.enchanted(), effect.affected(), lvl, item, source, target, damageMultiplier, cancelFlag);
            }
        }
    }

    // ============================================================================================================== //

    default void apply(
        boolean isSource, EnchantmentTarget enchanted, EnchantmentTarget affected,
        int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag
    ) {
        if (isSource && enchanted == EnchantmentTarget.ATTACKER) {
            applyOnSource(affected, lvl, item, source, target, damageMultiplier, cancelFlag);
            return;
        }
        if (!isSource && enchanted == EnchantmentTarget.VICTIM) {
            applyOnTarget(affected, lvl, item, source, target, damageMultiplier, cancelFlag);
        }
    }

    default void applyOnSource(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) { }

    default void applyOnTarget(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) { }


    MapCodec<? extends CriticalHitEffect> codec();

    // ============================================================================================================== //

    static Cancel cancel() { return new Cancel(); }

    record Cancel() implements CriticalHitEffect {

        public static final MapCodec<Cancel> CODEC = MapCodec.unit(Cancel::new);

        @Override
        public void apply(
            boolean isSource, EnchantmentTarget enchanted, EnchantmentTarget affected,
            int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag
        ) {
            cancelFlag.cancel();
        }

        @Override
        public MapCodec<Cancel> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static ChangeMultiplier changeMultiplier(EnchantmentValueEffect value) { return new ChangeMultiplier(value); }

    record ChangeMultiplier(EnchantmentValueEffect value) implements CriticalHitEffect {

        public static final MapCodec<ChangeMultiplier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(o -> o.value)
        ).apply(instance, ChangeMultiplier::new));

        @Override
        public void applyOnSource(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) {
            damageMultiplier.setValue(value.process(lvl, source.getRandom(), damageMultiplier.floatValue()));
        }

        @Override
        public void applyOnTarget(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) {
            damageMultiplier.setValue(value.process(lvl, target.getRandom(), damageMultiplier.floatValue()));
        }

        @Override
        public MapCodec<ChangeMultiplier> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static EntityEffect entityEffect(EnchantmentEntityEffect effect) { return new EntityEffect(effect); }

    record EntityEffect(EnchantmentEntityEffect effect) implements CriticalHitEffect {

        public static final MapCodec<EntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentEntityEffect.CODEC.fieldOf("effect").forGetter(o -> o.effect)
        ).apply(instance, EntityEffect::new));

        @Override
        public void applyOnSource(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) {
            var entity = affected == EnchantmentTarget.VICTIM ? target : source;
            effect.apply(source.serverLevel(), lvl, item, entity, entity.position());
        }

        @Override
        public void applyOnTarget(EnchantmentTarget affected, int lvl, EnchantedItemInUse item, ServerPlayer source, LivingEntity target, MutableFloat damageMultiplier, CancelFlag cancelFlag) {
            var entity = affected == EnchantmentTarget.VICTIM ? target : source;
            effect.apply((ServerLevel) target.level(), lvl, item, entity, entity.position());
        }

        @Override
        public MapCodec<EntityEffect> codec() { return CODEC; }

    }

}
