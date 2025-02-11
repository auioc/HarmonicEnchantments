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

package org.auioc.mcmod.harmonicench.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.auioc.mcmod.harmonicench.api.EnchantmentOnItemVisitor;
import org.auioc.mcmod.harmonicench.api.EnchantmentVisitor;

import java.util.Map;
import java.util.Optional;

public class HEHelper {

    public static <T> Optional<T> getEnchantmentEffect(Holder<Enchantment> ench, DataComponentType<T> type) {
        return Optional.ofNullable(ench.value().effects().get(type));
    }

    public static <T> Optional<T> getEnchantmentEffect(Holder<Enchantment> ench, DeferredHolder<DataComponentType<?>, DataComponentType<T>> type) {
        return getEnchantmentEffect(ench, type.get());
    }

    public static <T> void runIterationOnItem(ItemStack item, DataComponentType<T> type, EnchantmentVisitor<T> visitor) {
        EnchantmentHelper.runIterationOnItem(item, (ench, lvl) -> {
            HEHelper.getEnchantmentEffect(ench, type).ifPresent(effect -> {
                visitor.accept(ench, effect, lvl);
            });
        });
    }

    public static <T> void runIterationOnItem(ItemStack item, DeferredHolder<DataComponentType<?>, DataComponentType<T>> type, EnchantmentVisitor<T> visitor) {
        runIterationOnItem(item, type.get(), visitor);
    }

    public static <T> void runIterationOnEquipment(LivingEntity living, DataComponentType<T> type, EnchantmentOnItemVisitor<T> visitor) {
        EnchantmentHelper.runIterationOnEquipment(living, (ench, lvl, item) -> {
            HEHelper.getEnchantmentEffect(ench, type).ifPresent(effect -> {
                visitor.accept(ench, effect, lvl, item);
            });
        });
    }

    public static <T> void runIterationOnEquipment(LivingEntity living, DeferredHolder<DataComponentType<?>, DataComponentType<T>> type, EnchantmentOnItemVisitor<T> visitor) {
        runIterationOnEquipment(living, type.get(), visitor);
    }

    // ============================================================================================================== //

    public static Optional<EnchantedItemInUse> getItemInUse(LivingEntity owner, ItemStack item, Holder<Enchantment> ench) {
        return ench.value().getSlotItems(owner).entrySet().stream()
            .filter(entry -> entry.getValue() == item)
            .findAny()
            .map(Map.Entry::getKey)
            .map(slot -> new EnchantedItemInUse(item, slot, owner));
    }

    // ============================================================================================================== //

    public static LevelBasedValue ticksToSeconds(LevelBasedValue value) {
        return new LevelBasedValue.Fraction(value, LevelBasedValue.constant(0.05F));
    }

    // ============================================================================================================== //

    public static ItemEnchantments getAllEnchantments(ItemStack item) {
        var enchantments = item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        var lookup = CommonHooks.resolveLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
        if (lookup != null) {
            enchantments = item.getAllEnchantments(lookup);
        }

        return enchantments;
    }

}
