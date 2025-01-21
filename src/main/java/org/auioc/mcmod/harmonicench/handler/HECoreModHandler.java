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

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;

public class HECoreModHandler {

    public static int onApplyLootBonusCount(ItemStack loot, LootContext context, ItemStack tool, Holder<Enchantment> enchantment, int originalLvl) {
        var value = new MutableFloat(originalLvl);
        EnchantmentHelper.runIterationOnItem(tool, (ench, lvl) -> {
            var effects = ench.value().effects().get(HEEnchantmentEffectComponents.LOOT_BONUS_COUNT.get());
            if (effects != null) {
                Enchantment.applyEffects(effects, context, (effect) -> {
                    if (effect.enchantment().contains(enchantment)) {
                        value.setValue(effect.value().process(lvl, context.getRandom(), value.floatValue()));
                    }
                });
            }

        });
        return value.intValue();
    }

}
