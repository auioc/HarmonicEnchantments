/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicLib, a mod made for Minecraft.
 *
 * HarmonicLib is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmoniclib.mixin.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentPerformer;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ElytraItem.class)
public class MixinElytraItem {

    /**
     * @author WakelessSloth56
     * @reason {@link IItemEnchantment.Elytra#canElytraFly}
     */
    @Overwrite(remap = false)
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return EnchantmentPerformer.canElytraFly(stack, entity) && ElytraItem.isFlyEnabled(stack);
    }

}
