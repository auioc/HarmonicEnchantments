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

package org.auioc.mcmod.harmonicench.effect.impl;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.game.effect.MobEffectUtils;
import org.auioc.mcmod.harmonicench.effect.HEMobEffects;

public class WeightlessnessMobEffect extends MobEffect {

    public WeightlessnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }

    /**
     * FMLCoreMod: harmonicench.living_entity.travel
     */
    public static Vec3 adjustFallFlySpeed(LivingEntity living, Vec3 vec3) {
        int lvl = Math.min(MobEffectUtils.getLevel(living, HEMobEffects.WEIGHTLESSNESS.get()), 100);
        if (lvl > 0 && vec3.y < 0.0D) {
            double vY = vec3.y + Math.abs(vec3.y) * lvl * 0.01D;
            return new Vec3(vec3.x, vY, vec3.z);
        }
        return vec3;
    }

}
