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
import net.neoforged.neoforge.common.Tags;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>TODO 熟练 Proficiency</b>
 * <p>
 * 随着挖掘次数增加，永久提高挖掘速度。
 * <ul>
 *     <li>每次挖掘后，有 <code>[∑(n,k=1)(1/k)]/200</code> 概率使挖掘速度永久提高 1×。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ProficiencyEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.AXES, ItemTags.HOES, Tags.Items.TOOLS_SHEAR)
    );

    private static final ItemTagBuilder PRIMARY_ITEMS = primarySupportedItems((tag) -> tag.addTags(
        ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.AXES, ItemTags.HOES)
    );

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.EFFICIENCY,
        HEEnchantments.HARVEST
    );

    /**
     * Ⅰ:  1 - 61 <br>
     * Ⅱ: 11 - 71 <br>
     * Ⅲ: 21 - 81 <br>
     * Ⅳ: 31 - 91 <br>
     * Ⅴ: 41 - 101 <br>
     */
    private static final Cost COST = dynamicCost(1, 10, 61, 10);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        PRIMARY_ITEMS,
        EXCLUSIVE,
        Rarity.UNCOMMON,
        5,
        COST,
        1,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, PRIMARY_ITEMS, EXCLUSIVE).tradeable();
    }

}
