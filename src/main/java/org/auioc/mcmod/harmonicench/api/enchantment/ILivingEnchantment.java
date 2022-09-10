package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ILivingEnchantment {

    public static interface Death {

        void onLivingDeath(int lvl, LivingEntity target, DamageSource source);

    }


}
