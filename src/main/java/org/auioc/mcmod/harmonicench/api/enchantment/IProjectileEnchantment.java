package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.harmonicench.api.entity.IPotionArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class IProjectileEnchantment {

    public static interface HurtLiving {

        float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 ownerPostion, float amount);

    }

    public static interface AbstractArrow {

        void handleAbstractArrow(int lvl, net.minecraft.world.entity.projectile.AbstractArrow arrow);

    }

    public static interface PotionArrow {

        void handlePotionArrow(int lvl, net.minecraft.world.entity.projectile.Arrow arrow, IPotionArrow potionArrow);

    }

    public static interface SpectralArrow {

        void handleSpectralArrow(int lvl, net.minecraft.world.entity.projectile.SpectralArrow spectralArrow);

    }

}
