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
import org.auioc.mcmod.harmonicench.api.HEEnchantment;

/**
 * <b>TODO 献祭诅咒 Curse of Sacrificing</b>
 * <p>
 * 需要定期杀死生物。
 * <ul>
 *     <li>每20分钟（一个游戏日）需要使用带有献祭诅咒的物品杀死一个生物，否则该物品会消失。在装备栏或副手持有也可以，杀死生物时会重置计时器。</li>
 *     <li>物品因此消失时，给予玩家负等级的生命提升效果，持续时间为该物品所有魔咒等级之和，效果等级为魔咒总数。同时在对话框发送：“（物品）渴求祭品，在抽取了（玩家）的生命之后消失了。”</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SacrificingCurseEnchantment extends HEEnchantment {

    /**
     * Ⅰ:  25 - 50
     */
    private static final Cost COST = constantCost(25, 50);

    private static final BuilderFunction BUILDER = define(
        ItemTags.DURABILITY_ENCHANTABLE,
        Rarity.RARE,
        1,
        COST,
        8,
        EquipmentSlotGroup.ANY
    ).andThen((key, ctx, builder) -> builder

    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).curse().treasure().tradeable();
    }

}
