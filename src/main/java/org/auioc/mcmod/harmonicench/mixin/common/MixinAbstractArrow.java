package org.auioc.mcmod.harmonicench.mixin.common;

import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinAbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.projectile.AbstractArrow;

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
