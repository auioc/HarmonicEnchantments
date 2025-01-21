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

import net.minecraft.core.HolderSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.neoforged.neoforge.common.Tags;
import org.auioc.mcmod.arnicalib.game.loot.predicate.BlockStateCondition;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.effect.LootBonusCountEffect;

/**
 * <b>深层研究 Deep Study</b>
 * <p>
 * 开采深层矿石时，获得两倍等级时运的效果。
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class DeepStudyEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(Enchantments.FORTUNE);

    /**
     * Ⅰ: 15 - 61 <br>
     * Ⅱ: 24 - 71 <br>
     * Ⅲ: 33 - 81 <br>
     */
    private static final Cost COST = dynamicCost(15, 9, 65, 9);

    private static final BuilderFunction BUILDER = define(
        ItemTags.PICKAXES,
        EXCLUSIVE,
        Rarity.RARE,
        3,
        COST,
        4,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.LOOT_BONUS_COUNT.get(),
            new LootBonusCountEffect(HolderSet.direct(lookupEnchantment(ctx).getOrThrow(Enchantments.FORTUNE)), new AddValue(LevelBasedValue.perLevel(2))),
            () -> new BlockStateCondition(lookupBlock(ctx).getOrThrow(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).tradeable();
    }

}
