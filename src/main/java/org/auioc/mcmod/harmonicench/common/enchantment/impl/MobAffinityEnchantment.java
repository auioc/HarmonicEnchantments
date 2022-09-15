package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.server.event.impl.PiglinStanceEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class MobAffinityEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.PiglinStance {

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
        if (this.isValidSlot(slot)) {
            return PiglinStanceEvent.Stance.NEUTRAL;
        }
        return stance;
    }

}
