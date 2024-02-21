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

package org.auioc.mcmod.harmonicench.enchantment.impl;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IAttributeModifierEnchantment;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * <b>锻打 Forging</b>
 * <p>
 * 根据盔甲的总附魔数量，增加护甲值和盔甲韧性。
 * <ul>
 *     <li>增加 <code>x∑(n,k=1)(1/2k)</code> 点护甲值和盔甲韧性。（x：该物品魔咒数）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ForgingEnchantment extends HLEnchantment implements IAttributeModifierEnchantment {

    private static final EnchantmentCategory METALLIC_ARMOR = EnchantmentCategory.create(
        "METALLIC_ARMOR",
        (item) -> {
            if (item instanceof ArmorItem arrow && arrow.getMaterial() instanceof ArmorMaterials material) {
                return switch (material) {
                    case CHAIN, IRON, GOLD, NETHERITE -> true;
                    default -> false;
                };
            }
            return false;
        }
    );

    private static final UUID ARMOR_TOUGHNESS_MODIFIER_UUID = UUID.fromString("26E2A6CB-1EA6-401A-BD70-6ECB85D39969");
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("6EB3BD31-C501-4EFE-BE1E-900A9200A310");

    public ForgingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            METALLIC_ARMOR,
            new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET },
            (o) -> o != Enchantments.MENDING
                && o != HEEnchantments.DINING.get()
                && o != HEEnchantments.BLESSING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 41;
    }

    @Nullable
    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        if (itemStack.canEquip(slot, null)) {
            double x = itemStack.getAllEnchantments().size();
            if (x > 0.0D) {
                double bonus = x * MathUtil.sigma(lvl, 1, (double i) -> 1 / (2 * i));
                return Map.of(
                    Attributes.ARMOR,
                    new AttributeModifier(ARMOR_MODIFIER_UUID, this.descriptionId, bonus, AttributeModifier.Operation.ADDITION),
                    Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(ARMOR_TOUGHNESS_MODIFIER_UUID, this.descriptionId, bonus, AttributeModifier.Operation.ADDITION)
                );
            }
        }
        return null;
    }

}
