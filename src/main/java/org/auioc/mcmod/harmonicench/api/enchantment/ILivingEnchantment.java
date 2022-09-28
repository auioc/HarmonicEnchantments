package org.auioc.mcmod.harmonicench.api.enchantment;

import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ILivingEnchantment {

    public static interface Death {

        void onLivingDeath(int lvl, LivingEntity target, DamageSource source);

    }

    public static interface Hurt {

        float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount);

    }

    public static interface PiglinStance {

        MobStance onPiglinChooseStance(int lvl, EquipmentSlot slot, LivingEntity target, MobStance stance);

    }

    public static interface Cat {

        double onSetCatMorningGiftChance(int lvl, EquipmentSlot slot, net.minecraft.world.entity.animal.Cat cat, Player ownerPlayer, double chance);

    }

    public static interface Potion {

        void onPotionAdded(int lvl, EquipmentSlot slot, @Nullable Entity source, MobEffectInstance newEffect, MobEffectInstance oldEffect);

    }

}
