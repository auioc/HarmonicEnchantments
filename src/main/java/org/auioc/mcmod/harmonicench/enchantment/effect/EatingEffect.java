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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.auioc.mcmod.arnicalib.base.event.CancelFlag;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;

import java.util.function.Function;

public interface EatingEffect {

    ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends EatingEffect>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<EatingEffect> CODEC = REGISTRY.codec(ResourceLocation.CODEC).dispatch(EatingEffect::codec, Function.identity());

    static void bootstrap() {
        REGISTRY.put(HarmonicEnchantments.id("cancel"), Cancel.CODEC);
        REGISTRY.put(HarmonicEnchantments.id("repair_with_food"), RepairWithFood.CODEC);
    }

    // ============================================================================================================== //

    void apply(ServerPlayer player, int enchantmentLevel, EnchantedItemInUse item, ItemStack food, MutableInt nutrition, MutableFloat saturation, CancelFlag cancelFlag);

    MapCodec<? extends EatingEffect> codec();

    // ============================================================================================================== //

    static Cancel cancel() { return new Cancel(); }

    class Cancel implements EatingEffect {

        public static final MapCodec<Cancel> CODEC = MapCodec.unit(Cancel::new);

        @Override
        public void apply(ServerPlayer player, int enchantmentLevel, EnchantedItemInUse item, ItemStack food, MutableInt nutrition, MutableFloat saturation, CancelFlag cancelFlag) {
            cancelFlag.cancel();
        }

        @Override
        public MapCodec<Cancel> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static RepairWithFood repairWithFood(int preFoodPoint) { return new RepairWithFood(HEEnchantedValue.constant(preFoodPoint)); }

    record RepairWithFood(HEEnchantedValue preFoodPoint) implements EatingEffect {

        public static final MapCodec<RepairWithFood> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.fieldOf("pre_food_point").forGetter(o -> o.preFoodPoint)
        ).apply(instance, RepairWithFood::new));

        @Override
        public void apply(ServerPlayer player, int lvl, EnchantedItemInUse item, ItemStack food, MutableInt nutrition, MutableFloat saturation, CancelFlag cancelFlag) {
            var stack = item.itemStack();
            int n = nutrition.intValue();
            float s = saturation.floatValue();
            int u = Math.round(preFoodPoint.calculate(lvl, item));
            if (stack.isDamaged() && n > 0) {
                int damage = stack.getDamageValue();
                int c = (int) Math.ceil((double) damage / u);
                if (n >= c) {
                    s *= ((float) c) / ((float) n);
                    n -= c;
                    damage = 0;
                } else {
                    damage -= n * u;
                    s = 0.0F;
                    n = 0;
                }
                stack.setDamageValue(damage);
                nutrition.setValue(n);
                saturation.setValue(s);
            }
        }

        @Override
        public MapCodec<RepairWithFood> codec() { return CODEC; }

    }

}
