package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.harmonicench.server.event.impl.PiglinStanceEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class ILivingEnchantment {

    public static interface Death {

        void onLivingDeath(int lvl, LivingEntity target, DamageSource source);

    }

    public static interface Hurt {

        float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount);

    }

    public static interface PiglinStance {

        PiglinStanceEvent.Stance onPiglinChooseStance(int lvl, EquipmentSlot slot, LivingEntity target, PiglinStanceEvent.Stance stance);

    }

}
