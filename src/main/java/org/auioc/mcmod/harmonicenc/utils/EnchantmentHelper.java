package org.auioc.mcmod.harmonicenc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.auioc.mcmod.harmonicenc.api.enchantment.IAttributeModifierEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK)) return Optional.empty();
        var modifiers = new ArrayList<Map<Attribute, AttributeModifier>>();
        for (var enchEntry : getEnchantments(itemStack).entrySet()) {
            if (enchEntry.getKey() instanceof IAttributeModifierEnchantment ench) {
                ench.getAttributeModifiers(enchEntry.getValue(), slotType).ifPresent((m) -> modifiers.add(m));
            }
        }
        return Optional.of(modifiers);
    }

}
