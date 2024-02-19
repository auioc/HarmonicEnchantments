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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentPerformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PiglinAi.class)
public class MixinPiglinAi {

    @Inject(
        method = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isWearingGold(Lnet/minecraft/world/entity/LivingEntity;)Z",
        at = @At(value = "HEAD"),
        cancellable = true,
        require = 1,
        allow = 1
    )
    private static void isWearingGold(LivingEntity pLivingEntity, CallbackInfoReturnable<Boolean> cri) {
        var stance = EnchantmentPerformer.onPiglinCheckGoldArmor(pLivingEntity);
        if (stance == MobStance.NEUTRAL) {
            cri.setReturnValue(true);
        } else if (stance == MobStance.HOSTILE) {
            cri.setReturnValue(false);
        }
    }


}
