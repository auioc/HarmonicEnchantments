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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.auioc.mcmod.harmonicench.handler.HEMixinHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Debug(export = true)
@Mixin(Arrow.class)
public abstract class MixinArrow extends AbstractArrow {

    private MixinArrow(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_) {
        super(p_331098_, p_331626_);
    }

    @Shadow
    abstract PotionContents getPotionContents();

    @Overwrite
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        HEMixinHandler.handlerArrowPostHurtEffect((Arrow) (Object) this, living);
    }

}
