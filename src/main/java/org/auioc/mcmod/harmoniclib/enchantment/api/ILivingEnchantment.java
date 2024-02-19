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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.function.BiPredicate;

public class ILivingEnchantment {

    public static interface Death {

        void onLivingDeath(int lvl, ItemStack itemStack, LivingEntity target, DamageSource source);

    }

    public static interface Hurt {

        float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount);

    }

    public static interface Piglin {

        MobStance isWearingGold(int lvl, EquipmentSlot slot, LivingEntity target, MobStance stance);

    }

    public static interface Cat {

        void onCatMorningGiftConditionCheck(
            int lvl, EquipmentSlot slot, net.minecraft.world.entity.animal.Cat cat, Player owner, LinkedHashMap<ResourceLocation, BiPredicate<net.minecraft.world.entity.animal.Cat, Player>> conditions
        );

    }

    public static interface Potion {

        void onPotionAdded(int lvl, EquipmentSlot slot, @Nullable Entity source, MobEffectInstance newEffect, MobEffectInstance oldEffect);

    }

}
