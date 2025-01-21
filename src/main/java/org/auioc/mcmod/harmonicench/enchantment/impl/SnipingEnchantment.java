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
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;

/**
 * <b>TODO 狙击 Sniping</b>
 * <p>
 * 更容易命中，对远距离目标造成的伤害更高。
 * <ul>
 *     <li>对16格以外的实体造成伤害增加 <code>[(x-16)/32]∑(n,k=1)(1/k)</code> 倍。（对近处不会降低伤害）</li>
 *     <li>箭矢降低 15%/30%/45% 所受重力影响。（更高等级维持45%）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SnipingEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE,
        HEEnchantments.EFFICACY
    );

    /**
     * Ⅰ:  1 - 21 <br>
     * Ⅱ: 11 - 31 <br>
     * Ⅲ: 21 - 41 <br>
     */
    private static final Cost COST = dynamicCost(1, 10, 21, 10);

    private static final BuilderFunction BUILDER = define(
        ItemTags.CROSSBOW_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        3,
        COST,
        2,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
