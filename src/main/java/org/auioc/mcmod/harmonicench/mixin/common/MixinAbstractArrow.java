package org.auioc.mcmod.harmonicench.mixin.common;

import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinAbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractArrow;

@Mixin(value = AbstractArrow.class)
public class MixinAbstractArrow implements IMixinAbstractArrow {

    private double gravity = 0.05D;

    @Override
    public double getGravity() {
        return this.gravity;
    }

    @Override
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @ModifyArg(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;tick()V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V", ordinal = 0),
        index = 1,
        require = 1,
        allow = 1
    )
    private double modifyArg_tick(double p_20336_) {
        return ((AbstractArrow) (Object) this).getDeltaMovement().y - this.gravity;
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void readAdditionalSaveData(CompoundTag p_36761_, CallbackInfo ci) {
        if (p_36761_.contains("Gravity", 99)) {
            this.gravity = p_36761_.getDouble("Gravity");
        }
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/AbstractArrow;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void addAdditionalSaveData(CompoundTag p_36772_, CallbackInfo ci) {
        p_36772_.putDouble("Gravity", this.gravity);
    }

}
