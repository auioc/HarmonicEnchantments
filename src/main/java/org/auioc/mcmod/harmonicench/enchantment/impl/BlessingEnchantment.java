/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.enchantment.impl;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>TODO 祝福 Blessing</b>
 * <p>
 * 根据物品的所有魔咒等级之和，提供魔法抗性。
 * <ul>
 *     <li>增加 <code>∑(N,i=1)(6/i)×∑(n,j=1)[1/(5j-4)]</code> 点魔法抗性。（N：该物品所有魔咒等级之和）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BlessingEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.add(
        Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
        Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
        Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MENDING,
        HEEnchantments.FORGING, HEEnchantments.DINING
    );

    /**
     * Ⅰ: 1 - 51 <br>
     */
    private static final Cost COST = constantCost(1, 51);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        EXCLUSIVE,
        Rarity.RARE,
        1,
        COST,
        2,
        EquipmentSlotGroup.ARMOR
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable().treasure();
    }

}
