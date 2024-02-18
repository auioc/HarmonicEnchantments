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

package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public interface IAttributeModifierEnchantment extends IValidSlotsVisibleEnchantment {

    @Nullable
    Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack);

    default Optional<Map<Attribute, AttributeModifier>> getOptionalAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return isValidSlot(slot) ? Optional.ofNullable(getAttributeModifier(lvl, slot, itemStack)) : Optional.empty();
    }

}
