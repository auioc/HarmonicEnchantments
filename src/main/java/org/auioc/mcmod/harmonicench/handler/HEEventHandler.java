/*
 * Copyright (C) 2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.handler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.arnicalib.base.event.CancelFlag;
import org.auioc.mcmod.arnicalib.base.event.EventResult;
import org.auioc.mcmod.arnicalib.game.event.ItemAbilityCheckEvent;
import org.auioc.mcmod.arnicalib.game.event.ItemDamageEvent;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.effect.CriticalHitEffect;
import org.auioc.mcmod.harmonicench.loot.HELootContextParamSets;
import org.auioc.mcmod.harmonicench.utils.HEHelper;

import java.util.Map;

public class HEEventHandler {

    @SubscribeEvent
    public static void onCheckItemAbility(ItemAbilityCheckEvent event) {
        HEHelper.runIterationOnItem(event.getItem(), HEEnchantmentEffectComponents.ITEM_ABILITIES, (ench, effect, lvl) -> {
            event.setResult(effect.getOrDefault(event.getAbility(), EventResult.DEFAULT));
        });
    }

    @SubscribeEvent
    public static void onItemDamaged(ItemDamageEvent event) {
        var owner = event.getEntity();
        if (owner != null) {
            var item = event.getItem();
            HEHelper.runIterationOnItem(item, HEEnchantmentEffectComponents.ITEM_DAMAGED, (ench, effects, lvl) -> {
                ench.value().getSlotItems(owner).entrySet().stream()
                    .filter(entry -> entry.getValue() == item).findAny().map(Map.Entry::getKey).ifPresent(
                        slot -> {
                            var itemInUse = new EnchantedItemInUse(item, slot, owner);
                            var deltaDamage = event.getNewDamage() - event.getOriginalDamage();
                            Enchantment.applyEffects(
                                effects,
                                HELootContextParamSets.enchantedItemWithEntity((ServerLevel) owner.level(), lvl, item, owner, owner.position()),
                                effect -> effect.apply(lvl, itemInUse, owner, deltaDamage)
                            );
                        }
                    );
            });
        }
    }

    @SubscribeEvent
    public static void onEnderPearlLand(EntityTeleportEvent.EnderPearl event) {
        var player = event.getPlayer();
        HEHelper.runIterationOnEquipment(player, HEEnchantmentEffectComponents.ENDER_PEARL_LANDED, (ench, effects, lvl, item) -> {
            var damage = new MutableFloat(event.getAttackDamage());
            var cancelFlag = new CancelFlag();
            Enchantment.applyEffects(
                effects,
                Enchantment.entityContext(player.serverLevel(), lvl, player, player.position()),
                (effect) -> effect.apply(player, lvl, item, event.getPearlEntity(), damage, event.getHitResult(), cancelFlag)
            );
            event.setAttackDamage(damage.floatValue());
            event.setCanceled(cancelFlag.isCanceled());
        });
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide()) {
            HEHelper.runIterationOnEquipment(living, HEEnchantmentEffectComponents.DIMENSION_TRAVEL, (ench, effects, lvl, item) -> {
                var cancelFlag = new CancelFlag();
                Enchantment.applyEffects(
                    effects,
                    Enchantment.entityContext((ServerLevel) living.level(), lvl, living, living.position()),
                    (effect) -> effect.apply(living, lvl, item, event.getDimension(), cancelFlag)
                );
                event.setCanceled(cancelFlag.isCanceled());
            });
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if (event.isCriticalHit() && event.getEntity() instanceof ServerPlayer source && event.getTarget() instanceof LivingEntity target) {
            var damageMultiplier = new MutableFloat(event.getDamageMultiplier());
            var cancelFlag = new CancelFlag();
            HEHelper.runIterationOnEquipment(source, HEEnchantmentEffectComponents.CRITICAL_HIT, (ench, effects, lvl, item) -> {
                CriticalHitEffect.apply(
                    effects,
                    HELootContextParamSets.enchantedDirectAttack(source.serverLevel(), lvl, item.itemStack(), source, target),
                    true,
                    lvl, item, source, target, damageMultiplier, cancelFlag
                );
            });
            HEHelper.runIterationOnEquipment(target, HEEnchantmentEffectComponents.CRITICAL_HIT, (ench, effects, lvl, item) -> {
                CriticalHitEffect.apply(
                    effects,
                    HELootContextParamSets.enchantedDirectAttack((ServerLevel) target.level(), lvl, item.itemStack(), source, target),
                    false,
                    lvl, item, source, target, damageMultiplier, cancelFlag
                );
            });
            event.setDamageMultiplier(damageMultiplier.floatValue());
            if (cancelFlag.isCanceled()) {
                event.setCriticalHit(false);
            }
        }
    }

    //    @SubscribeEvent
    //    public static void onGetEnchantments(GetEnchantmentLevelEvent event) {
    //        event.getEnchantments().removeIf((e) -> true);
    //    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            HEHelper.runIterationOnEquipment(player, HEEnchantmentEffectComponents.BLOCK_DESTROYED, (ench, effects, lvl, item) -> {
                var pos = Vec3.atCenterOf(event.getPos());
                var level = player.serverLevel();
                Enchantment.applyEffects(
                    effects,
                    HELootContextParamSets.enchantedBlockDestroyed(level, lvl, item.itemStack(), player, pos, event.getState()),
                    (effect) -> effect.apply(level, lvl, item, player, pos)
                );
            });
        }
    }


}
