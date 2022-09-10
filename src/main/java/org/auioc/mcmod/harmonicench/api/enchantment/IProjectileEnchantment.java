package org.auioc.mcmod.harmonicench.api.enchantment;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public interface IProjectileEnchantment {

    default float onHitLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, @Nullable Vec3 ownerPostion, float amount) {
        return amount;
    }

}
