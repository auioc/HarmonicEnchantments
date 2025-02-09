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

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.effect.EatingEffect;

/**
 * <b>饱食修补 Dining</b>
 * <p>
 * 食用食物后，将玩家增加的饥饿值转化为该物品的耐久度。
 * <ul>
 *     <li>每 1 点饥饿值增加 10 点耐久度。</li>
 * </ul>
 */
public class DiningEnchantment extends HEEnchantment {

    private static final ItemTagBuilder PRIMARY_ITEMS = primarySupportedItems(t -> { });

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MENDING, Enchantments.INFINITY,
        HEEnchantments.FREE_RIDING, HEEnchantments.DINING, HEEnchantments.FORGING, HEEnchantments.BLESSING
    );

    /**
     * Ⅰ: 25 - 75 <br>
     */
    private static final Cost COST = constantCost(25, 75);

    private static final int REPAIR_PRE_FOOD_POINT = 10;

    private static final BuilderFunction BUILDER = define(
        ItemTags.DURABILITY_ENCHANTABLE,
        PRIMARY_ITEMS,
        EXCLUSIVE,
        Rarity.RARE,
        1,
        COST,
        4,
        EquipmentSlotGroup.ANY
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.EATING.get(),
            EatingEffect.repairWithFood(REPAIR_PRE_FOOD_POINT)
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(PRIMARY_ITEMS, EXCLUSIVE).tradeable().treasure();
    }

}
