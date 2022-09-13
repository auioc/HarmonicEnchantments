package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * @see org.auioc.mcmod.harmonicench.server.event.HEServerEventHandler#onEnderPearlTeleport
 * @see org.auioc.mcmod.harmonicench.server.event.HEServerEventHandler#onEntityTravelToDimension
 */
public class SafeTeleportingEnchantment extends AbstractHEEnchantment {

    public SafeTeleportingEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET, 4);
    }

    public int getMinCost(int lvl) {
        return 5 + (lvl - 1) * 6;
    }

    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 6;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FALL_PROTECTION;
    }

    public static void handleLivingTravelToDimension(int lvl, LivingEntity living) {
        double duration = 0.0D;
        for (int k = 1, n = lvl + 1; k < n; k++) duration += 20.0D / ((double) k);
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) (duration * 20)));
    }

}
