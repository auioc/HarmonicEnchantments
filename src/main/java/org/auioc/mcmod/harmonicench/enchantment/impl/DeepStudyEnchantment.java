/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.Tags;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILootBonusEnchantment;

/**
 * <b>深层研究 Deep Study</b>
 * <p>
 * 开采深层矿石时，获得两倍等级时运的效果。
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class DeepStudyEnchantment extends HLEnchantment implements ILootBonusEnchantment.ApplyBonusCountFunction {

    public DeepStudyEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.PICKAXE,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.BLOCK_FORTUNE
        );
    }

    // Ⅰ: 15 - 61
    // Ⅱ: 24 - 71
    // Ⅲ: 33 - 81
    @Override
    public int getMinCost(int lvl) {
        return lvl * 9 + 6;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 45 + lvl;
    }

    @Override
    public int onApplyLootEnchantmentBonusCount(int lvl, LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        if (
            enchantment == Enchantments.BLOCK_FORTUNE
                && lootContext.getParam(LootContextParams.BLOCK_STATE).is(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
        ) {
            return enchantmentLevel + (lvl * 2);
        }
        return enchantmentLevel;
    }

}
