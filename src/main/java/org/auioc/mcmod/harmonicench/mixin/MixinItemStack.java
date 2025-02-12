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

package org.auioc.mcmod.harmonicench.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.auioc.mcmod.harmonicench.handler.HEMixinHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(value = ItemStack.class)
public class MixinItemStack {

    /**
     * @see <a href="https://github.com/auioc/arnicalib-mcmod/blob/v6.1.3/src/main/java/org/auioc/mcmod/arnicalib/mod/mixin/common/MixinItemStack.java">ArncaLib v6: MixinItemStack.java</a>
     */
    @Inject(
        method = "Lnet/minecraft/world/item/ItemStack;inventoryTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;IZ)V",
        at = @At(
            value = "JUMP",
            opcode = Opcodes.IFNULL,
            shift = At.Shift.AFTER
        ),
        require = 1,
        allow = 1
    )
    private void inventoryTick(Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        // TODO ?move to arnicalib
        // TODO ?event
        // entity is always Player, inventoryTick is only called from net.minecraft.world.entity.player.Inventory#tick
        HEMixinHandler.onInventoryTick((Player) entity, level, ((ItemStack) (Object) this), inventorySlot, isCurrentItem);
    }

}
