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

package org.auioc.mcmod.harmoniclib.enchantment.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.game.entity.projectile.ITippedArrow;

public class IProjectileEnchantment {

    public static interface HurtLiving {

        float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 ownerPostion, float amount);

    }

    public static interface AbstractArrow {

        void handleAbstractArrow(int lvl, net.minecraft.world.entity.projectile.AbstractArrow arrow);

    }

    public static interface TippedArrow {

        void handleTippedArrow(int lvl, net.minecraft.world.entity.projectile.Arrow arrow, ITippedArrow potionArrow);

    }

    public static interface SpectralArrow {

        void handleSpectralArrow(int lvl, net.minecraft.world.entity.projectile.SpectralArrow spectralArrow);

    }

    public static interface FireworkRocket {

        void handleFireworkRocket(int lvl, FireworkRocketEntity fireworkRocket);

        float onFireworkRocketExplode(int lvl, LivingEntity target, FireworkRocketEntity projectile, LivingEntity owner, float amount);

    }

}
