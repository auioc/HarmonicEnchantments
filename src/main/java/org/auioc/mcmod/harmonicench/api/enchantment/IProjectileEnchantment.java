package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public interface IProjectileEnchantment {

    default float onHitLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 postion, float amount) {
        return amount;
    }

}
