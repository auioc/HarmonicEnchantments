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

package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.game.random.GameRandomUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
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

public class ElectrificationEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Tick.Inventory, ILivingEnchantment.Hurt {

    public ElectrificationEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.TRIDENT,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND },
            5,
            (o) -> o != Enchantments.RIPTIDE && o != Enchantments.IMPALING
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1 + (lvl - 1) * 8;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
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
