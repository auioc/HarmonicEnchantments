/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentHelper;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IBlockEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * <b>熟练 Proficiency</b>
 * <p>
 * 随着挖掘次数增加，永久提高挖掘速度。
 * <ul>
 *     <li>每次挖掘后，有 <code>[∑(n,k=1)(1/k)]/200</code> 概率使挖掘速度永久提高 1×。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ProficiencyEnchantment extends HLEnchantment implements IBlockEnchantment.BreakSpeed, IBlockEnchantment.Break, IItemEnchantment.Tooltip {

    public static final String NBT_PROFICIENCY = "Proficiency";

    public ProficiencyEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            EnchantmentCategory.DIGGER,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> o != Enchantments.BLOCK_EFFICIENCY && o != HEEnchantments.HARVEST.get()
        );
    }

    // Ⅰ:  1 - 61
    // Ⅱ: 11 - 71
    // Ⅲ: 21 - 81
    // Ⅳ: 31 - 91
    // Ⅴ: 41 - 101
    @Override
    public int getMinCost(int lvl) {
        return lvl * 10 - 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 60;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShearsItem || super.canEnchant(itemStack);
    }

    private static int getProficiency(ItemStack itemStack) {
        return (itemStack.hasTag()) ? itemStack.getTag().getInt(NBT_PROFICIENCY) : 0;
    }

    @Override
    public void onBlockBreak(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            if (player.getRandom().nextDouble() < (MathUtil.sigma(lvl, 1, (double i) -> 1.0D / i) / 200.0)) {
                var nbt = itemStack.getOrCreateTag();
                int proficiency = nbt.getInt(NBT_PROFICIENCY) + 1;
                nbt.putInt(NBT_PROFICIENCY, proficiency);
            }
        }
    }

    @Override
    public float getBreakSpeed(int lvl, ItemStack itemStack, Player player, BlockState blockState, Optional<BlockPos> blockPos, float speed) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            int proficiency = getProficiency(itemStack);
            if (proficiency > 0) return speed + proficiency;
        }
        return speed;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(int lvl, @NotNull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        if (EnchantmentHelper.NOT_BOOK.test(itemStack)) {
            EnchantmentHelper.getEnchantmentTooltip(lines, this.getDescriptionId())
                .ifPresent(
                    (text) -> ((MutableComponent) text)
                        .append(" ")
                        .append(Component.translatable(getDescriptionId() + ".proficiency", getProficiency(itemStack)))
                );
        }
    }

}
