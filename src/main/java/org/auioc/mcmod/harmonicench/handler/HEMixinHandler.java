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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.enchantment.Enchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.utils.HEHelper;

import java.util.ArrayList;

public class HEMixinHandler {

    public static void handlerArrowPostHurtEffect(Arrow arrow, LivingEntity target) {
        if (target.level().isClientSide()) {
            return;
        }

        var mobEffects = new ArrayList<MobEffectInstance>();
        var potionContents = arrow.getPotionContents();
        potionContents.potion().ifPresent(p -> p.value().getEffects().stream()
            .map(i -> new MobEffectInstance(
                i.getEffect(),
                Math.max(i.mapDuration(t -> t / 8), 1), i.getAmplifier(),
                i.isAmbient(), i.isVisible()
            )).forEach(mobEffects::add));
        mobEffects.addAll(potionContents.customEffects());

        var source = arrow.getEffectSource();

        var weapon = arrow.getWeaponItem();
        if (weapon != null) {
            HEHelper.runIterationOnItem(weapon, HEEnchantmentEffectComponents.ARROW_POTION, (ench, effects, lvl) -> {
                var lootContext = Enchantment.itemContext((ServerLevel) target.level(), lvl, weapon);
                Enchantment.applyEffects(
                    effects, lootContext,
                    e -> mobEffects.replaceAll(original -> e.apply(original, lvl, source))
                );
            });
        }

        mobEffects.forEach(e -> target.addEffect(e, source));
    }

}
