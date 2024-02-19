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

package org.auioc.mcmod.harmoniclib.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.advancement.predicate.HEPerformancePredicateType;
import org.auioc.mcmod.harmoniclib.advancement.predicate.IHEPerformancePredicate;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("rawtypes")
public class EnchantmentPerformedTrigger extends SimpleCriterionTrigger<EnchantmentPerformedTrigger.TriggerInstance> {

    @SuppressWarnings("unchecked")
    public <P extends IHEPerformancePredicate> void trigger(ServerPlayer player, Enchantment enchantment, ItemStack itemStack, Predicate<P> performancePredicate) {
        this.trigger(player, (instance) -> ((TriggerInstance<P>) instance).matches(enchantment, itemStack, performancePredicate));
    }

    @Override
    public Codec<EnchantmentPerformedTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public static <P extends IHEPerformancePredicate> Criterion<EnchantmentPerformedTrigger.TriggerInstance> createCriterion(
        Optional<ContextAwarePredicate> player, Enchantment enchantment, Optional<ItemPredicate> item, Optional<P> performance
    ) {
        return HLCriteriaTriggers.ENCHANTMENT_PERFORMED.get().createCriterion(
            new EnchantmentPerformedTrigger.TriggerInstance<>(player, enchantment, item, performance)
        );
    }

    public static <P extends IHEPerformancePredicate> Criterion<EnchantmentPerformedTrigger.TriggerInstance> createCriterion(
        @Nullable ContextAwarePredicate player, Enchantment enchantment, @Nullable ItemPredicate item, @Nullable P performance
    ) {
        return createCriterion(Optional.ofNullable(player), enchantment, Optional.ofNullable(item), Optional.ofNullable(performance));
    }

    // ============================================================================================================== //

    public record TriggerInstance<P extends IHEPerformancePredicate>(
        Optional<ContextAwarePredicate> player,
        Enchantment enchantment,
        Optional<ItemPredicate> item,
        Optional<P> performance
    ) implements SimpleCriterionTrigger.SimpleInstance {

        public boolean matches(Enchantment enchantment, ItemStack itemStack, Predicate<P> performancePredicate) {
            return this.enchantment.equals(enchantment)
                && (item.isEmpty() || item.get().matches(itemStack))
                && (performance.isEmpty() || performancePredicate.test(performance.get()));
        }

        public static final Codec<EnchantmentPerformedTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(TriggerInstance::player),
            BuiltInRegistries.ENCHANTMENT.byNameCodec().fieldOf("enchantment").forGetter(TriggerInstance::enchantment),
            ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(TriggerInstance::item),
            ExtraCodecs.strictOptionalField(HEPerformancePredicateType.CODEC, "performance").forGetter(TriggerInstance::performance)
        ).apply(instance, TriggerInstance::new));

    }

}
