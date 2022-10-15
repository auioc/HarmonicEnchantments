package org.auioc.mcmod.harmonicench.common.mobeffect.impl;

import org.auioc.mcmod.arnicalib.game.effect.MobEffectUtils;
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
        int lvl = Math.min(MobEffectUtils.getLevel(living, HEMobEffects.WEIGHTLESSNESS.get()), 100);
        if (lvl > 0 && vec3.y < 0.0D) {
            double vY = vec3.y + Math.abs(vec3.y) * lvl * 0.01D;
            return new Vec3(vec3.x, vY, vec3.z);
        }
        return vec3;
    }

}
