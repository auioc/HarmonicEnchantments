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

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.harmonicench.handler.HEMixinHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiConsumer;

@Debug(export = true)
@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

    @Inject(
        method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;forEachModifier(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1,
        cancellable = true
    )
    private static void forEachModifier(ItemStack stack, EquipmentSlotGroup slotGroup, BiConsumer<Holder<Attribute>, AttributeModifier> action, CallbackInfo ci) {
        HEMixinHandler.forEachModifier(stack, slotGroup, action);
    }

    @Inject(
        method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;forEachModifier(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1,
        cancellable = true
    )
    private static void forEachModifier(ItemStack stack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> action, CallbackInfo ci) {
        HEMixinHandler.forEachModifier(stack, slot, action);
    }

    @Inject(
        method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageProtection(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;)F",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/commons/lang3/mutable/MutableFloat;floatValue()F",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        require = 1,
        allow = 1,
        cancellable = true
    )
    private static void getDamageProtection(
        ServerLevel level, LivingEntity entity, DamageSource damageSource,
        CallbackInfoReturnable<Float> cir,
        MutableFloat mutablefloat
    ) {
        HEMixinHandler.getDamageProtection(level, entity, damageSource, mutablefloat);
    }

}
