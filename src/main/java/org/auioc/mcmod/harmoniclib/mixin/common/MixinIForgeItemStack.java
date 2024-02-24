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

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentPerformer;
import org.auioc.mcmod.harmoniclib.enchantment.api.IToolActionControllerEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = IItemStackExtension.class, remap = false)
public interface MixinIForgeItemStack {

    /**
     * @author WakelessSloth56
     * @reason {@link IToolActionControllerEnchantment#canPerformAction}
     */
    @Overwrite
    default boolean canPerformAction(ToolAction toolAction) {
        var self = ((ItemStack) (Object) this);
        if (!EnchantmentPerformer.canPerformAction(self, toolAction)) {
            return false;
        }
        return self.getItem().canPerformAction(self, toolAction);
    }

}
