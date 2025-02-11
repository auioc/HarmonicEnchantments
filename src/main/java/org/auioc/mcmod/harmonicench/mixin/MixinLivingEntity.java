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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.auioc.mcmod.harmonicench.handler.HEMixinHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    //    @Inject(
    //        method = "Lnet/minecraft/world/entity/LivingEntity;canGlide()Z",
    //        at = @At(value = "HEAD"),
    //        require = 1,
    //        allow = 1,
    //        cancellable = true
    //    )
    //    private static void canGlide(CallbackInfoReturnable<Boolean> cir) {
    //        cir.setReturnValue(false);
    //    }

    @Redirect(
        method = "Lnet/minecraft/world/entity/LivingEntity;canGlide()Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
        ),
        require = 1,
        allow = 1
    )
    private boolean redirect_canGlide_canGlideUsing(ItemStack stack, EquipmentSlot slot) {
        return HEMixinHandler.canGlideUsing(((LivingEntity) (Object) this), stack, slot);
    }

    @Redirect(
        method = "Lnet/minecraft/world/entity/LivingEntity;lambda$updateFallFlying$17(Lnet/minecraft/world/entity/EquipmentSlot;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
        ),
        require = 1,
        allow = 1
    )
    private boolean redirect_lambda$updateFallFlying$17_canGlideUsing(ItemStack stack, EquipmentSlot slot) {
        return HEMixinHandler.canGlideUsing(((LivingEntity) (Object) this), stack, slot);
    }

    //    @Redirect(
    //        method = "Lnet/minecraft/world/entity/LivingEntity;getEffectiveGravity()D",
    //        at = @At(
    //            value = "INVOKE",
    //            target = "Lnet/minecraft/world/entity/LivingEntity;getGravity()D"
    //        )
    //    )
    //    private void redirect_getEffectiveGravity_getGravity(LivingEntity living) { }

}
