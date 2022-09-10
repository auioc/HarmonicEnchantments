package org.auioc.mcmod.harmonicench.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment;
import org.auioc.mcmod.harmonicench.api.entity.IEnchantableEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static void runIteration(BiConsumer<Enchantment, Integer> visitor, Map<Enchantment, Integer> enchMap) {
        for (var enchEntry : enchMap.entrySet()) {
            visitor.accept(enchEntry.getKey(), enchEntry.getValue());
        }
    }

    public static void runIterationOnItem(BiConsumer<Enchantment, Integer> visitor, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;
        for (var enchEntry : getEnchantments(itemStack).entrySet()) {
            visitor.accept(enchEntry.getKey(), enchEntry.getValue());
        }
    }

    public static void runIterationOnItems(BiConsumer<Enchantment, Integer> visitor, Iterable<ItemStack> itemStacks, Predicate<ItemStack> predicate) {
        for (var itemStack : itemStacks) {
            if (predicate.test(itemStack)) {
                runIterationOnItem(visitor, itemStack);
            }
        }
    }

    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK)) return Optional.empty();
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

    public static void copyItemEnchantmentsToEntity(ItemStack itemStack, Entity entity, BiPredicate<Enchantment, Integer> predicate) {
        if (entity instanceof IEnchantableEntity _entity) {
            for (var enchEntry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                if (predicate.test(enchEntry.getKey(), enchEntry.getValue())) {
                    _entity.addEnchantment(enchEntry.getKey(), enchEntry.getValue());
                }
            }
        }
    }

    public static float onProjectileHurtLiving(LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 postion, float originalAmount) {
        var enchMap = ((IEnchantableEntity) projectile).getEnchantments();
        if (enchMap == null) return originalAmount;

        var amount = new MutableFloat(originalAmount);
        runIteration(
            (ench, lvl) -> {
                if (ench instanceof IProjectileEnchantment.HurtLiving _ench) {
                    amount.setValue(_ench.onHurtLiving(lvl, target, projectile, owner, postion, amount.floatValue()));
                }
            },
            enchMap
        );
        return amount.floatValue();
    }

    public static void onLivingDeath(LivingEntity target, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            runIterationOnItems(
                (ench, lvl) -> {
                    if (ench instanceof ILivingEnchantment.Death _ench) {
                        _ench.onLivingDeath(lvl, target, source);
                    }
                },
                sourceLiving.getAllSlots(),
                (itemStack) -> !itemStack.is(Items.ENCHANTED_BOOK)
            );
        }
    }

}
