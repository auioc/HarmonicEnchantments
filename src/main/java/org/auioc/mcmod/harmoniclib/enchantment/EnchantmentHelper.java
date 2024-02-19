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

package org.auioc.mcmod.harmoniclib.enchantment;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.auioc.mcmod.arnicalib.base.collection.ListUtils;
import org.auioc.mcmod.arnicalib.game.enchantment.IEnchantmentAttachableObject;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static final Predicate<ItemStack> NOT_BOOK = (itemStack) -> !itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> NOT_ITEM = (itemStack) -> itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> IS_ITEM = (itemStack) -> !itemStack.isEmpty() && !itemStack.is(Items.ENCHANTED_BOOK);

    public static void copyItemEnchantmentsToEntity(ItemStack itemStack, Entity entity, BiPredicate<Enchantment, Integer> predicate) {
        if (entity instanceof IEnchantmentAttachableObject _entity) {
            for (var enchEntry : getEnchantments(itemStack).entrySet()) {
                if (predicate.test(enchEntry.getKey(), enchEntry.getValue())) {
                    _entity.addEnchantment(enchEntry.getKey(), enchEntry.getValue());
                }
            }
        }
    }

    public static void copyItemEnchantmentsToEntity(ItemStack itemStack, Entity entity) {
        copyItemEnchantmentsToEntity(itemStack, entity, (ench, lvl) -> true);
    }

    @OnlyIn(Dist.CLIENT)
    public static Optional<Component> getEnchantmentTooltip(List<Component> tooltip, String key) {
        int i = ListUtils.indexOf(tooltip, (l) -> {
            if (l.getContents() instanceof TranslatableContents t) {
                if (t.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        });
        return (i >= 0) ? Optional.of(tooltip.get(i)) : Optional.empty();
    }

}
