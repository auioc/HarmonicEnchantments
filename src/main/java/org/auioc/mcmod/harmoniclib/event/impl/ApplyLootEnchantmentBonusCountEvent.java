/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicLib, a mod made for Minecraft.
 *
 * HarmonicLib is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmoniclib.event.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.bus.api.Event;

public class ApplyLootEnchantmentBonusCountEvent extends Event {

    private final LootContext lootContext;
    private final ItemStack itemStack;
    private final Enchantment enchantment;
    private int enchantmentLevel;

    public ApplyLootEnchantmentBonusCountEvent(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        this.lootContext = lootContext;
        this.itemStack = itemStack;
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
    }

    public LootContext getLootContext() {
        return this.lootContext;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

}
