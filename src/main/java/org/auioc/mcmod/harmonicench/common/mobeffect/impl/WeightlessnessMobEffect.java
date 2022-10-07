package org.auioc.mcmod.harmonicench.common.mobeffect.impl;

import org.auioc.mcmod.arnicalib.game.effect.EffectUtils;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WeightlessnessMobEffect extends MobEffect {

    public WeightlessnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }

    public static Vec3 adjustFallFlySpeed(LivingEntity living, Vec3 vec3) {
        int lvl = Math.min(EffectUtils.getEffectLevel(living, HEMobEffects.WEIGHTLESSNESS.get()), 100);
        if (lvl > 0) {
            return vec3;
        }
        return vec3;
    }

}
