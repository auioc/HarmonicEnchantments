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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.auioc.mcmod.arnicalib.game.random.GameRandomUtils;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

/**
 * <b>感电 Electrification</b>
 * <p>
 * 降雨或雷暴天气时，有概率产生闪电击中持有者并给予力量效果。
 * <ul>
 *     <li>降雨（雷暴）时，每一秒有 1%（5%）概率产生闪电击中持有者。</li>
 *     <li>持有者被任何闪电击中后，获得力量效果，等级为 <code>⌊∑(n,k=1)[5/(2k+1)]⌋</code> 级，持续 <code>10×∑(n,k=1)(1/k)</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ElectrificationEnchantment extends HLEnchantment implements IItemEnchantment.Tick.Inventory, ILivingEnchantment.Hurt {

    public ElectrificationEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.TRIDENT,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND },
            5,
            (o) -> o != Enchantments.RIPTIDE && o != Enchantments.IMPALING
        );
    }

    // Ⅰ:  1 - 21
    // Ⅱ:  9 - 29
    // Ⅲ: 17 - 37
    // Ⅳ: 25 - 45
    // Ⅴ: 33 - 53
    @Override
    public int getMinCost(int lvl) {
        return lvl * 8 - 7;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 20;
    }

    @Override
    public void onInventoryTick(int lvl, ItemStack itemStack, Player player, Level level, int index, boolean selected) {
        if (!selected) return;
        if (level.isClientSide) return;

        if (player.tickCount % 20 != 0) return;

        var serverLevel = (ServerLevel) level;

        int chance;
        if (player.isInWaterOrRain() && !player.isInWater()) {
            if (serverLevel.isThundering()) {
                chance = 5;
            } else if (serverLevel.isRaining()) {
                chance = 1;
            } else {
                return;
            }
        } else {
            return;
        }

        if (GameRandomUtils.percentageChance(100 - chance, player.getRandom())) {
            return;
        }

        var lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
        lightning.setPos(player.position());
        serverLevel.addFreshEntity(lightning);
    }

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (!isSource && source == target.damageSources().lightningBolt()) {
            double effectDuration = 0.0D;
            double effectLevel = 0.0D;
            for (int k = 1, n = lvl + 1; k < n; k++) {
                effectDuration += 15.0D * (1.0D / ((double) k));
                effectLevel += 5.0D / (2.0D * ((double) k) + 1.0D);
            }
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, ((int) effectDuration) * 20, ((int) effectLevel) - 1));
        }
        return amount;
    }

}
