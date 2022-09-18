package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.server.event.impl.PiglinStanceEvent;
import org.auioc.mcmod.arnicalib.utils.game.EffectUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class MobAffinityEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.PiglinStance, ILivingEnchantment.Cat, ILivingEnchantment.Potion {

    public MobAffinityEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}, 1);
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 41;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.AQUA_AFFINITY;
    }

    @Override
    public PiglinStanceEvent.Stance onPiglinChooseStance(int lvl, EquipmentSlot slot, LivingEntity target, PiglinStanceEvent.Stance stance) {
        return PiglinStanceEvent.Stance.NEUTRAL;
    }

    @Override
    public double onSetCatMorningGiftChance(int lvl, EquipmentSlot slot, Cat cat, Player ownerPlayer, double chance) {
        return 1.0D;
    }

    @Override
    public void onPotionAdded(int lvl, EquipmentSlot slot, Entity source, MobEffectInstance newEffect, MobEffectInstance oldEffect) {
        if (newEffect.getEffect() == MobEffects.BAD_OMEN) {
            EffectUtils.setDuration(newEffect, 0);
        } else if (source != null) {
            if (source.getType() == EntityType.AXOLOTL && newEffect.getEffect() == MobEffects.REGENERATION) {
                EffectUtils.setAmplifier(newEffect, 1);
            } else if (source.getType() == EntityType.DOLPHIN && newEffect.getEffect() == MobEffects.DOLPHINS_GRACE) {
                EffectUtils.setDuration(newEffect, 10 * 20);
            }
        }
    }

}
