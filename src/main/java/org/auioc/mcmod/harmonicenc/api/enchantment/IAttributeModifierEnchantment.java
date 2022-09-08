package org.auioc.mcmod.harmonicenc.api.enchantment;

import java.util.Map;
import java.util.Optional;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IAttributeModifierEnchantment extends IValidSlotsVisibleEnchantment {

    Map<Attribute, AttributeModifier> getAttributeModifiers(int lvl);

    default Optional<Map<Attribute, AttributeModifier>> getAttributeModifiers(int lvl, EquipmentSlot slot) {
        for (var _slot : getValidSlots()) {
            if (slot.equals(_slot)) {
                return Optional.of(getAttributeModifiers(lvl));
            }
        }
        return Optional.empty();
    }

}
