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

/**
 * <b>TODO 亲善 Mob Affinity</b>
 * <p>
 * 穿戴有此附魔的盔甲时，能更好地得到友好生物们的帮助
 * <ul>
 *     <li>村民：不会获得不祥之兆。</li>
 *     <li>猪灵：视为玩家穿戴了金质盔甲。</li>
 *     <li>美西螈：给予玩家的生命恢复Ⅰ变为生命恢复Ⅱ。</li>
 *     <li>海豚：给予玩家的海豚的恩惠，持续时间变为 10 秒。</li>
 *     <li>猫：玩家醒来时，猫给予礼物的概率从 70% 提高到 100%。</li>
 * </ul>
 *
 * @author LainIO24
 */
public class MobAffinityEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.HEAD_ARMOR, ItemTags.CHEST_ARMOR, ItemTags.LEG_ARMOR, ItemTags.FOOT_ARMOR
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.AQUA_AFFINITY);

    /**
     * Ⅰ: 1 - 41 <br>
     */
    private static final Cost COST = constantCost(1, 41);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.HEAD_ARMOR,
        EXCLUSIVE,
        Rarity.VERY_RARE,
        1,
        COST,
        4,
        EquipmentSlotGroup.ARMOR
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }


}
