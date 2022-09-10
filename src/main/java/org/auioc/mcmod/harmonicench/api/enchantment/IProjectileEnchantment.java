package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class IProjectileEnchantment {

    public static interface HurtLiving {

        float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 ownerPostion, float amount);

    }

}
