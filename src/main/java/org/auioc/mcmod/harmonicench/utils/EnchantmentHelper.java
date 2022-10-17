package org.auioc.mcmod.harmonicench.utils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.auioc.mcmod.arnicalib.game.enchantment.IEnchantmentAttachableObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static final Predicate<ItemStack> NOT_BOOK = (itemStack) -> !itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> NOT_ITEM = (itemStack) -> itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> IS_ITEM = (itemStack) -> !itemStack.isEmpty() && !itemStack.is(Items.ENCHANTED_BOOK);

    public static void copyItemEnchantmentsToEntity(ItemStack itemStack, Entity entity, BiPredicate<Enchantment, Integer> predicate) {
        if (entity instanceof IEnchantmentAttachableObject _entity) {
            for (var enchEntry : getEnchantments(itemStack).entrySet()) {
                if (predicate.test(enchEntry.getKey(), enchEntry.getValue())) {
                    _entity.addEnchantment(enchEntry.getKey(), enchEntry.getValue());
                }
            }
        }
    }

    public static void copyItemEnchantmentsToEntity(ItemStack itemStack, Entity entity) {
        copyItemEnchantmentsToEntity(itemStack, entity, (ench, lvl) -> true);
    }

}
