package org.auioc.mcmod.harmonicench.mixin.common;

import javax.annotation.Nullable;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinIndirectEntityDamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

@Mixin(value = IndirectEntityDamageSource.class)
public class MixinIndirectEntityDamageSource implements IMixinIndirectEntityDamageSource {

    @Final
    @Nullable
    private Vec3 indirectSourcePosition;

    @Override
    public Vec3 getIndirectSourcePosition() {
        return this.indirectSourcePosition;
    }

    @Inject(
        method = "Lnet/minecraft/world/damagesource/IndirectEntityDamageSource;<init>(Ljava/lang/String;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V",
        at = @At(value = "RETURN"),
        require = 1,
        allow = 1
    )
    public void IndirectEntityDamageSource(
        String p_19406_, Entity p_19407_, @Nullable Entity p_19408_, CallbackInfo ci
    ) {
        if (p_19408_ != null) {
            this.indirectSourcePosition = p_19408_.position();
        }
    }

}
