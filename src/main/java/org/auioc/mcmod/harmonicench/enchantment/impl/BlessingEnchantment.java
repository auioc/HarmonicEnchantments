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

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.collection.ListUtils;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * <b>祝福 Blessing</b>
 * <p>
 * 根据物品的所有魔咒等级之和，提供魔法抗性。
 * <ul>
 *     <li>增加 <code>∑(N,i=1)(6/i)×∑(n,j=1)[1/(5j-4)]</code> 点魔法抗性。（N：该物品所有魔咒等级之和）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BlessingEnchantment extends HLEnchantment implements IItemEnchantment.Protection, IItemEnchantment.Tooltip {

    private static final EnchantmentCategory BLESSABLE_ARMOR = EnchantmentCategory.create(
        "BLESSABLE_ARMOR",
        (item) -> {
            if (item instanceof ArmorItem arrow && arrow.getMaterial() instanceof ArmorMaterials material) {
                switch (material) {
                    case LEATHER:
                    case GOLD:
                    case NETHERITE: {
                        return true;
                    }
                    default: {
                        break;
                    }
                }
            }
            return false;
        }
    );

    private static final EquipmentSlot[] VALID_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public BlessingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            BLESSABLE_ARMOR,
            VALID_SLOTS,
            (o) -> o != Enchantments.MENDING && o != HEEnchantments.FORGING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 51;
    }

    private static int calcMagicProtection(int lvl, ItemStack itemStack) {
        int N = EnchantmentHelper.getEnchantments(itemStack).values().stream().mapToInt((i) -> i).sum();
        double l = MathUtil.sigma(N, 1, (double i) -> 6.0D / i);
        double r = MathUtil.sigma(lvl, 1, (double j) -> 1.0D / (5.0D * j - 4.0D));
        return (int) (l * r);
    }

    @Override
    public int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source) {
        if (source.is(DamageTypes.MAGIC)) {
            return calcMagicProtection(lvl, itemStack);
        }
        return 0;
    }

    private static final List<String> TOOLTIP_MODIFIER_KEYS = Arrays.stream(VALID_SLOTS).map((slot) -> "item.modifiers." + slot.getName()).toList();

    @Override
    public void onItemTooltip(int lvl, @NotNull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        if (ItemStack.shouldShowInTooltip(itemStack.getHideFlags(), ItemStack.TooltipPart.MODIFIERS)) { // TODO arnicalib
            int i = ListUtils.indexOf(
                lines,
                (l) -> l.getContents() instanceof TranslatableContents t && TOOLTIP_MODIFIER_KEYS.contains(t.getKey())
            );
            if (i >= 0) {
                var text = Component.translatable(
                        "attribute.modifier.plus.0",
                        calcMagicProtection(lvl, itemStack),
                        Component.translatable("enchantment.harmonicench.blessing.tooltip")
                    )
                    .withStyle(ChatFormatting.BLUE);
                lines.add(i + 1, text);
            }
        }
    }

}
