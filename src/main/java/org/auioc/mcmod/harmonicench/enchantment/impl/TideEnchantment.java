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

/**
 * <b>TODO 唤潮 Tide</b>
 * <p>
 * 提高水中投掷速度，击中目标会使其窒息。
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.0
 */
public class TideEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.IMPALING, Enchantments.LOYALTY
    );

    /**
     * Ⅰ:  1 - 21 <br>
     * Ⅱ:  9 - 29 <br>
     * Ⅲ: 17 - 37 <br>
     * Ⅳ: 25 - 45 <br>
     * Ⅴ: 33 - 53 <br>
     */
    private static final Cost COST = dynamicCost(1, 8, 21, 8);

    private static final BuilderFunction BUILDER = define(
        ItemTags.TRIDENT_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        5,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
