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

package org.auioc.mcmod.harmoniclib.enchantment.api;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import org.auioc.mcmod.arnicalib.game.registry.RegistryUtils;
import org.auioc.mcmod.harmoniclib.config.NullableBooleanValue;

import java.util.function.Predicate;

public abstract class AbstractHEEnchantment extends Enchantment implements IValidSlotsVisibleEnchantment, IConfigurableEnchantment {

    protected final int maxLevel;
    protected final Predicate<Enchantment> compatibility;
    protected final NullableBooleanValue isEnabled = new NullableBooleanValue(() -> HEEnchantmentManager.getConfigEnabled(RegistryUtils.id(this)));

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlots);
        this.maxLevel = maxLevel;
        this.compatibility = compatibility;
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel, Predicate<Enchantment> compatibility) {
        this(rarity, category, new EquipmentSlot[] { validSlot }, maxLevel, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, Predicate<Enchantment> compatibility) {
        this(rarity, category, validSlots, 1, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, Predicate<Enchantment> compatibility) {
        this(rarity, category, validSlot, 1, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel) {
        this(rarity, category, validSlot, maxLevel, (o) -> true);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel) {
        this(rarity, category, validSlots, maxLevel, (o) -> true);
    }

    public AbstractHEEnchantment() {
        this(Rarity.COMMON, EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND, 1, (o) -> true);
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    protected boolean checkCompatibility(Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && compatibility.test(otherEnchantment);
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return isEnabled() && this.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return isEnabled() && super.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return isEnabled() && super.isAllowedOnBooks();
    }

    @Override
    public boolean isDiscoverable() {
        return isEnabled();
    }

    @Override
    public boolean isTradeable() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled.getValue();
    }

}
