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

package org.auioc.mcmod.harmoniclib.enchantment.api;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.auioc.mcmod.arnicalib.base.tuple.IntPair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class IItemEnchantment {

    public static interface Hurt {

        int onItemHurt(int lvl, ItemStack itemStack, int damage, RandomSource random, ServerPlayer player);

    }

    public static interface Protection {

        int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source);

    }

    public static interface Elytra {

        boolean canElytraFly(int lvl, ItemStack elytra, LivingEntity living);

    }

    public static interface FishingRod {

        IntPair preFishingRodCast(int lvl, ItemStack fishingRod, ServerPlayer player, int speedBonus, int luckBonus);

    }

    public static class Tick {

        public static interface Inventory {

            void onInventoryTick(int lvl, ItemStack itemStack, Player player, Level level, int index, boolean selected);

        }

    }

    public static interface Tooltip {

        @OnlyIn(Dist.CLIENT)
        void onItemTooltip(int lvl, @NotNull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags);

    }

}
