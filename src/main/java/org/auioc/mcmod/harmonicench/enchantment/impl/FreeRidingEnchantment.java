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
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.SetValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>无偿骑乘 Free Riding</b>
 * <p>
 * 骑乘猪或炽足兽时，使用提速不再消耗耐久。
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class FreeRidingEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.add(
        Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MENDING, Enchantments.UNBREAKING,
        HEEnchantments.REBELLING_CURSE
        // TODO       HEEnchantments.DINING
    );


    /**
     * Ⅰ: 25 - 75 <br>
     */
    private static final Cost COST = constantCost(25, 75);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        EXCLUSIVE,
        Rarity.VERY_RARE,
        1,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            EnchantmentEffectComponents.ITEM_DAMAGE,
            new SetValue(LevelBasedValue.constant(0F))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).treasure().tradeable();
    }

}
