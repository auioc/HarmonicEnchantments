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

package org.auioc.mcmod.harmoniclib.client.event;

import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.auioc.mcmod.harmoniclib.effect.IMovementInputMobEffect;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentPerformer;

@OnlyIn(Dist.CLIENT)
public final class HEClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        EnchantmentPerformer.onItemTooltip(event.getItemStack(), event.getEntity(), event.getToolTip(), event.getFlags());
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        var input = event.getInput();
        var player = (LocalPlayer) event.getEntity();
        for (var entry : player.getActiveEffectsMap().entrySet()) {
            if (entry.getKey() instanceof IMovementInputMobEffect effect) {
                effect.onMovementInputUpdate(entry.getValue().getAmplifier(), input, player);
            }
        }
    }

}
