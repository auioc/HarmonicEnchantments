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

package org.auioc.mcmod.harmoniclib.event;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.harmoniclib.event.impl.ApplyLootEnchantmentBonusCountEvent;
import org.auioc.mcmod.harmoniclib.event.impl.CatMorningGiftEvent;
import org.auioc.mcmod.harmoniclib.mixin.server.MixinCatRelaxOnOwnerGoal;

public final class HLServerEventFactory {

    private static final IEventBus BUS = NeoForge.EVENT_BUS;

    /**
     * FMLCoreMod: harmoniclib.apply_bonus_count.run
     */
    public static int onApplyLootEnchantmentBonusCount(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        var event = new ApplyLootEnchantmentBonusCountEvent(lootContext, itemStack, enchantment, enchantmentLevel);
        BUS.post(event);
        return event.getEnchantmentLevel();
    }

    /**
     * @see MixinCatRelaxOnOwnerGoal#stop
     */
    public static boolean onCatMorningGiftConditionCheck(Cat cat, Player owner) {
        var event = new CatMorningGiftEvent.Check(cat, owner);
        BUS.post(event);
        if (event.isCanceled()) {
            return false;
        }
        return event.check();
    }

}
