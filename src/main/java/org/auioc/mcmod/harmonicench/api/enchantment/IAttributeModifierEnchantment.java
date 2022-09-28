package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public interface IAttributeModifierEnchantment extends IValidSlotsVisibleEnchantment {

    @Nullable
    Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack);

    default Optional<Map<Attribute, AttributeModifier>> getOptionalAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return isValidSlot(slot) ? Optional.ofNullable(getAttributeModifier(lvl, slot, itemStack)) : Optional.empty();
    }

}
