package org.auioc.mcmod.harmonicenc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.auioc.mcmod.harmonicenc.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicenc.api.enchantment.IToolActionControllerEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ToolAction;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    private static void runIterationOnItem(BiConsumer<Enchantment, Integer> visitor, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;
        for (var enchEntry : getEnchantments(itemStack).entrySet()) {
            visitor.accept(enchEntry.getKey(), enchEntry.getValue());
        }
    }

    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK)) Optional.empty();
        var modifiers = new ArrayList<Map<Attribute, AttributeModifier>>();
        runIterationOnItem((ench, lvl) -> {
            if (ench instanceof IAttributeModifierEnchantment _ench) {
                _ench.getAttributeModifiers(lvl, slotType).ifPresent((m) -> modifiers.add(m));
            }
        }, itemStack);
        return Optional.of(modifiers);
    }

    public static boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {
        if (itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK)) return true;
        var bool = new MutableBoolean(true);
        runIterationOnItem((ench, lvl) -> {
            if (ench instanceof IToolActionControllerEnchantment _ench) {
                bool.setValue(bool.booleanValue() & _ench.canPerformAction(toolAction));
            }
        }, itemStack);
        return bool.booleanValue();
    }

}
