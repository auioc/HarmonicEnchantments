/*
 * Copyright (C) 2024-2025 AUIOC.ORG
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
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>TODO 收割 Harvest</b>
 * <p>
 * 提高收割作物（以及生物）的速度。
 * <ul>
 *     <li>成功挖掘农作物时，会同时挖掘切比雪夫距离 1/2/3 格内的同种农作物（更高等级维持3格）。</li>
 *     <li>攻击生命值在 15%/30%/45% 以下的目标时，将直接杀死目标并获得对应的头颅（更高等级维持45%）。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.1
 */
public class HarvestEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.HOES, ItemTags.SHOVELS
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.EFFICIENCY, HEEnchantments.PROFICIENCY
    );

    /**
     * Ⅰ: 15 - 61 <br>
     * Ⅱ: 24 - 71 <br>
     * Ⅲ: 33 - 81 <br>
     */
    private static final Cost COST = dynamicCost(15, 9, 61, 9);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.HOES,
        EXCLUSIVE,
        Rarity.VERY_RARE,
        3,
        COST,
        1,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
