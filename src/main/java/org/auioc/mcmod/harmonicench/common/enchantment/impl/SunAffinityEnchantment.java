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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.TickEvent;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;


public class SunAffinityEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Elytra, IPlayerEnchantment.Tick {

    public SunAffinityEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.ELYTRA,
            EquipmentSlot.CHEST,
            3
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 * lvl;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 30;
    }

    @Override
    public boolean canElytraFly(int lvl, ItemStack elytra, LivingEntity living) {
        var level = living.level();

        if (!level.dimensionType().natural()) return false;

        int dayTime = (int) (level.getDayTime() % 24000L);
        if (!(dayTime >= 0 && dayTime < 12000)) return false;

        if (living.isInWaterOrRain()) return false;

        return true;
    }

    @Override
    public void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, TickEvent.Phase phase, LogicalSide side) {
        if (phase == TickEvent.Phase.END && side == LogicalSide.SERVER) {
            if (player.isFallFlying()) {
                player.addEffect(new MobEffectInstance(HEMobEffects.WEIGHTLESSNESS.get(), 5, (Math.min(lvl, this.getMaxLevel()) * 20) - 1, false, true, false));
            } else {
                player.removeEffect(HEMobEffects.WEIGHTLESSNESS.get());
            }
        }
    }

}
