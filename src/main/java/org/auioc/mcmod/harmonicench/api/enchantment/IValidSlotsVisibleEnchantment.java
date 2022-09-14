package org.auioc.mcmod.harmonicench.api.enchantment;

import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.world.entity.EquipmentSlot;

public interface IValidSlotsVisibleEnchantment {

    default EquipmentSlot[] getValidSlots() {
        return new EquipmentSlot[] {};
    };

    default boolean isValidSlot(EquipmentSlot slot) {
        return ArrayUtils.contains(getValidSlots(), slot);
    }

}
