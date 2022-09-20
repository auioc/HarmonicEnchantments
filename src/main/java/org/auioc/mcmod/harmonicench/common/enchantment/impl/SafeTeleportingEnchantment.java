package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.utils.java.MathUtil;
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
        super(
            Enchantment.Rarity.UNCOMMON,
            EnchantmentCategory.ARMOR_FEET,
            EquipmentSlot.FEET,
            4,
            (o) -> o != Enchantments.FALL_PROTECTION
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 5 + (lvl - 1) * 6;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 6;
    }

    public static void handleLivingTravelToDimension(int lvl, LivingEntity living) {
        double duration = MathUtil.sigma(lvl, 1, (double i) -> 20.0D / i);
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) (duration * 20)));
    }

}
