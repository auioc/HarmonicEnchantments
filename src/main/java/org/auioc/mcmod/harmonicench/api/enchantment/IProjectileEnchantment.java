package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.arnicalib.game.entity.projectile.ITippedArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class IProjectileEnchantment {

    public static interface HurtLiving {

        float onHurtLiving(int lvl, LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 ownerPostion, float amount);

    }

    public static interface AbstractArrow {

        void handleAbstractArrow(int lvl, net.minecraft.world.entity.projectile.AbstractArrow arrow);

    }

    public static interface TippedArrow {

        void handleTippedArrow(int lvl, net.minecraft.world.entity.projectile.Arrow arrow, ITippedArrow potionArrow);

    }

    public static interface SpectralArrow {

        void handleSpectralArrow(int lvl, net.minecraft.world.entity.projectile.SpectralArrow spectralArrow);

    }

    public static interface FireworkRocket {

        void handleFireworkRocket(int lvl, FireworkRocketEntity fireworkRocket);

        float onFireworkRocketExplode(int lvl, LivingEntity target, FireworkRocketEntity projectile, LivingEntity owner, float amount);

    }

}
