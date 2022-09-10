package org.auioc.mcmod.harmonicench.mixin.common;

import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinSpectralArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.entity.projectile.SpectralArrow;

@Mixin(value = SpectralArrow.class)
public class MixinSpectralArrow implements IMixinSpectralArrow {

    @Shadow
    private int duration;

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

}
