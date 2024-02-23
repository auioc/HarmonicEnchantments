/*
 * Copyright (C) 2024 AUIOC.ORG
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
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.auioc.mcmod.arnicalib.game.codec.EnumCodec;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentPredicate;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentHelper;

import java.util.Optional;

public class LootEnchantmentAppliedTrigger extends SimpleCriterionTrigger<LootEnchantmentAppliedTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack itemStack) {
        this.trigger(player, (instance) -> instance.matches(itemStack));
    }

    public void triggerLootContext(ItemStack itemStack, LootContext lootCtx) {
        var entity = lootCtx.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity instanceof ServerPlayer player) {
            trigger(player, itemStack);
        }
    }

    public static Criterion<LootEnchantmentAppliedTrigger.TriggerInstance> createCriterion(
        Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints count, Optional<HEnchantmentPredicate> enchantment, Optional<Requirement> requirement
    ) {
        return HLCriteriaTriggers.LOOT_ENCHANTMENT_APPLIED.get().createCriterion(
            new TriggerInstance(player, item, count, enchantment, requirement)
        );
    }

    // ============================================================================================================== //

    public record TriggerInstance(
        Optional<ContextAwarePredicate> player,
        Optional<ItemPredicate> item,
        MinMaxBounds.Ints count,
        Optional<HEnchantmentPredicate> enchantment,
        Optional<Requirement> requirement

    ) implements SimpleCriterionTrigger.SimpleInstance {

        public boolean matches(ItemStack itemStack) {
            if (item.isPresent() && !item.get().matches(itemStack)) {
                return false;
            }

            var enchMap = EnchantmentHelper.getEnchantments(itemStack);

            if (count != MinMaxBounds.Ints.ANY && (enchMap.isEmpty() || !count.matches(enchMap.size()))) {
                return false;
            }

            if (enchantment.isPresent()) {
                if (enchMap.isEmpty()) {
                    return false;
                }

                var enchP = enchantment.get();
                int i = (int) enchMap.entrySet().stream().filter(enchP::test).count();

                var req = requirement.orElse(Requirement.ANY);
                if (!req.matches(enchMap.size(), i)) {
                    return false;
                }
            }

            return true;
        }

        // ========================================================================================================== //

        public static final Codec<LootEnchantmentAppliedTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(TriggerInstance::item),
            ExtraCodecs.strictOptionalField(MinMaxBounds.Ints.CODEC, "count", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::count),
            ExtraCodecs.strictOptionalField(HEnchantmentPredicate.CODEC, "enchantment").forGetter(TriggerInstance::enchantment),
            ExtraCodecs.strictOptionalField(EnumCodec.byNameLowerCase(Requirement.class), "enchantment_requirement").forGetter(TriggerInstance::requirement)
        ).apply(instance, LootEnchantmentAppliedTrigger.TriggerInstance::new));

    }

    // ============================================================================================================== //

    public enum Requirement {
        ANY {
            @Override
            public boolean matches(int size, int i) {
                return i > 0;
            }
        },
        ALL {
            @Override
            public boolean matches(int size, int i) {
                return size == i;
            }
        },
        NONE {
            @Override
            public boolean matches(int size, int i) {
                return i == 0;
            }
        };

        public boolean matches(int size, int i) { return false; }
    }

}
