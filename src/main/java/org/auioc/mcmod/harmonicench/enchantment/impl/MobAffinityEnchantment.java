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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.auioc.mcmod.arnicalib.game.effect.MobEffectUtils;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.AbstractHEEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

import java.util.LinkedHashMap;
import java.util.function.BiPredicate;

public class MobAffinityEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.Piglin, ILivingEnchantment.Cat, ILivingEnchantment.Potion {

    public MobAffinityEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            EnchantmentCategory.WEARABLE,
            new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET },
            1,
            (o) -> o != Enchantments.AQUA_AFFINITY
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 41;
    }

    @Override
    public MobStance isWearingGold(int lvl, EquipmentSlot slot, LivingEntity target, MobStance stance) {
        return MobStance.NEUTRAL;
    }

    @Override
    public void onPotionAdded(int lvl, EquipmentSlot slot, Entity source, MobEffectInstance newEffect, MobEffectInstance oldEffect) {
        if (newEffect.getEffect() == MobEffects.BAD_OMEN) {
            MobEffectUtils.setDuration(newEffect, 0);
        } else if (source != null) {
            if (source.getType() == EntityType.AXOLOTL && newEffect.getEffect() == MobEffects.REGENERATION) {
                MobEffectUtils.setAmplifier(newEffect, 1);
            } else if (source.getType() == EntityType.DOLPHIN && newEffect.getEffect() == MobEffects.DOLPHINS_GRACE) {
                MobEffectUtils.setDuration(newEffect, 10 * 20);
            }
        }
    }

    private static final ResourceLocation CAT_GIFT_CONDITION_VANILLA_RANDOM = new ResourceLocation("random");
    private static final ResourceLocation CAT_GIFT_CONDITION_ID = HarmonicEnchantments.id("mob_affinity_random");
    private static final BiPredicate<Cat, Player> CAT_GIFT_CONDITION = (cat, owner) -> true;

    @Override
    public void onCatMorningGiftConditionCheck(int lvl, EquipmentSlot slot, Cat cat, Player owner, LinkedHashMap<ResourceLocation, BiPredicate<Cat, Player>> conditions) {
        conditions.remove(CAT_GIFT_CONDITION_VANILLA_RANDOM);
        conditions.put(CAT_GIFT_CONDITION_ID, CAT_GIFT_CONDITION);
    }

}
