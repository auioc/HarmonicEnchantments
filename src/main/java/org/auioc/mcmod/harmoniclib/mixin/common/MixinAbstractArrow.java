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

package org.auioc.mcmod.harmoniclib.mixin.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.auioc.mcmod.harmoniclib.mixinapi.IMixinAbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractArrow.class)
public class MixinAbstractArrow implements IMixinAbstractArrow {

    private static final EntityDataAccessor<Float> GRAVITY_OFFSET = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.FLOAT);

    @Override
    public float getGravityOffset() {
        return ((AbstractArrow) (Object) this).getEntityData().get(GRAVITY_OFFSET);
    }

    @Override
    public void setGravityOffset(float offset) {
        ((AbstractArrow) (Object) this).getEntityData().set(GRAVITY_OFFSET, offset);
    }

    // ====================================================================== //

    @ModifyArg(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V",
            ordinal = 0
        ),
        index = 1,
        require = 1,
        allow = 1
    )
    private double modifyArg_tick(double p_20336_) {
        return p_20336_ + (double) getGravityOffset();
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;defineSynchedData()V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void defineSynchedData(CallbackInfo ci) {
        ((AbstractArrow) (Object) this).getEntityData().define(GRAVITY_OFFSET, 0.0F);
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void readAdditionalSaveData(CompoundTag p_36761_, CallbackInfo ci) {
        if (p_36761_.contains("GravityOffset", 99)) {
            setGravityOffset(p_36761_.getFloat("GravityOffset"));
        }
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void addAdditionalSaveData(CompoundTag p_36772_, CallbackInfo ci) {
        p_36772_.putFloat("GravityOffset", getGravityOffset());
    }

}
