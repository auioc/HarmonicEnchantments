package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.world.entity.EquipmentSlot;

public interface IValidSlotsVisibleEnchantment {

    default EquipmentSlot[] getValidSlots() {
        return new EquipmentSlot[] {};
    };

}
