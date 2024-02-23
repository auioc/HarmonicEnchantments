/*
 * Copyright (C) 2024 AUIOC.ORG
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

package org.auioc.mcmod.harmoniclib.mixin.server;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.advancement.criterion.LootEnchantmentAppliedTrigger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EnchantWithLevelsFunction.class)
public class MixinEnchantWithLevelsFunction {

    @Final
    @Shadow
    private NumberProvider levels;

    @Final
    @Shadow
    private boolean treasure;

    /**
     * @author WakelessSloth56
     * @reason LootEnchantmentAppliedTrigger {@link LootEnchantmentAppliedTrigger#triggerLootContext}
     */
    @Overwrite
    public ItemStack run(ItemStack pStack, LootContext pContext) {
        RandomSource randomsource = pContext.getRandom();
        var newItemStack = EnchantmentHelper.enchantItem(randomsource, pStack, this.levels.getInt(pContext), this.treasure);
        HLCriteriaTriggers.LOOT_ENCHANTMENT_APPLIED.get().triggerLootContext(newItemStack, pContext);
        return newItemStack;
    }


}
