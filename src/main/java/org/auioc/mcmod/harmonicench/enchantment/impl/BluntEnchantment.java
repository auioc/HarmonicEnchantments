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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.tag.HItemTags;
import org.auioc.mcmod.harmonicench.effect.HEMobEffects;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IPlayerEnchantment;

import java.util.Map;
import java.util.UUID;

/**
 * <b>钝重Blunt</b>
 * <p>
 * 降低攻击速度，大幅提高暴击伤害（跳劈）。
 * <ul>
 *     <li>降低 25% 攻击速度。</li>
 *     <li>暴击将造成 <code>0.5×(n+3)×100% </code>伤害。</li>
 *     <li>用红砖或下界砖暴击命中玩家，将给予混乱效果，持续时间 <code>∑(n,k=1)(5/k)</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BluntEnchantment extends HLEnchantment implements IAttributeModifierEnchantment, IPlayerEnchantment.CriticalHit {

    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("F839F42C-4B26-6F66-7025-1EF3294EED97");

    public BluntEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            HEnchantmentCategory.AXE,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> o != Enchantments.SHARPNESS
                && o != Enchantments.SMITE
                && o != Enchantments.BANE_OF_ARTHROPODS
                && o != Enchantments.BLOCK_EFFICIENCY
                && o != HEEnchantments.BANE_OF_CHAMPIONS.get()
                && o != HEEnchantments.RAPIER.get()
        );
    }

    // Ⅰ:  5 - 25
    // Ⅱ: 13 - 33
    // Ⅲ: 21 - 41
    // Ⅳ: 29 - 49
    // Ⅴ: 37 - 57
    @Override
    public int getMinCost(int lvl) {
        return lvl * 8 - 3;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof SwordItem || isBrick(itemStack) || super.canEnchant(itemStack);
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return Map.of(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(
                ATTACK_SPEED_UUID, this.descriptionId,
                isBrick(itemStack) ? -0.95D : -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );
    }

    @Override
    public float onCriticalHit(int lvl, ItemStack itemStack, Player player, Entity target, float damageModifier) {
        if (isBrick(itemStack) && target instanceof Player targetPlayer) {
            targetPlayer.addEffect(new MobEffectInstance(HEMobEffects.CONFUSION.get(), (int) MathUtil.sigma(lvl, 1, (double i) -> 5.0D / i) * 20));
        }
        return damageModifier + (0.5F * lvl);
    }

    // ====================================================================== //

    private static boolean isBrick(ItemStack itemStack) {
        return itemStack.is(HItemTags.BRICKS);
    }

}
