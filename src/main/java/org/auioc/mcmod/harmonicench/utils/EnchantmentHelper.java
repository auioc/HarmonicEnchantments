package org.auioc.mcmod.harmonicench.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.TriConsumer;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment;
import org.auioc.mcmod.harmonicench.api.entity.IEnchantableEntity;
import org.auioc.mcmod.harmonicench.api.function.QuadConsumer;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinArrow;
import org.auioc.mcmod.harmonicench.server.event.impl.PiglinStanceEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static final Predicate<ItemStack> NOT_BOOK = (itemStack) -> !itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> NOT_ITEM = (itemStack) -> itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> IS_ITEM = (itemStack) -> !itemStack.isEmpty() && !itemStack.is(Items.ENCHANTED_BOOK);


    public static void runIteration(BiConsumer<Enchantment, Integer> visitor, Map<Enchantment, Integer> enchMap) {
        for (var enchEntry : enchMap.entrySet()) visitor.accept(enchEntry.getKey(), enchEntry.getValue());
    }

    public static void runIterationOnItem(BiConsumer<Enchantment, Integer> visitor, ItemStack itemStack) {
        if (!itemStack.isEmpty()) runIteration(visitor, getEnchantments(itemStack));
    }

    public static void runIterationOnItems(TriConsumer<ItemStack, Enchantment, Integer> visitor, Iterable<ItemStack> itemStacks, Predicate<ItemStack> predicate) {
        for (var itemStack : itemStacks) {
            if (!itemStack.isEmpty() && predicate.test(itemStack)) {
                runIterationOnItem((ench, lvl) -> visitor.accept(itemStack, ench, lvl), itemStack);
            }
        }
    }

    public static void runIterationOnLiving(QuadConsumer<EquipmentSlot, ItemStack, Enchantment, Integer> visitor, LivingEntity living, EquipmentSlot[] slots) {
        for (EquipmentSlot slot : slots) {
            var itemStack = living.getItemBySlot(slot);
            runIterationOnItem((ench, lvl) -> visitor.accept(slot, itemStack, ench, lvl), itemStack);
        }
    }

    public static void runIterationOnLiving(QuadConsumer<EquipmentSlot, ItemStack, Enchantment, Integer> visitor, LivingEntity living) {
        runIterationOnLiving(visitor, living, EquipmentSlot.values());
    }


    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (NOT_ITEM.test(itemStack)) return Optional.empty();
        var modifiers = new ArrayList<Map<Attribute, AttributeModifier>>();
        runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IAttributeModifierEnchantment _ench) {
                    _ench.getAttributeModifiers(lvl, slotType).ifPresent((m) -> modifiers.add(m));
                }
            },
            itemStack
        );
        return Optional.of(modifiers);
    }

    public static boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {
        if (NOT_ITEM.test(itemStack)) return true;
        var bool = new MutableBoolean(true);
        runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IToolActionControllerEnchantment _ench) {
                    bool.setValue(bool.booleanValue() & _ench.canPerformAction(toolAction));
                }
            },
            itemStack
        );
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

    public static float onProjectileHurtLiving(LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 shootingPosition, float originalAmount) {
        var enchMap = ((IEnchantableEntity) projectile).getEnchantments();
        if (enchMap == null) return originalAmount;

        var amount = new MutableFloat(originalAmount);
        runIteration(
            (ench, lvl) -> {
                if (ench instanceof IProjectileEnchantment.HurtLiving _ench) {
                    amount.setValue(_ench.onHurtLiving(lvl, target, projectile, owner, shootingPosition, amount.floatValue()));
                }
            },
            enchMap
        );
        return amount.floatValue();
    }

    public static void handleArrow(ItemStack weapon, AbstractArrow abstractArrow) {
        runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IProjectileEnchantment.AbstractArrow _ench) {
                    _ench.handleAbstractArrow(lvl, abstractArrow);
                }
                if (ench instanceof IProjectileEnchantment.TippedArrow _ench && abstractArrow instanceof Arrow arrow) {
                    var potionArrow = (IMixinArrow) arrow;
                    if (potionArrow.getPotion() != Potions.EMPTY || !potionArrow.getEffects().isEmpty()) {
                        _ench.handleTippedArrow(lvl, arrow, potionArrow);
                    }
                }
                if (ench instanceof IProjectileEnchantment.SpectralArrow _ench && abstractArrow instanceof SpectralArrow spectralArrow) {
                    _ench.handleSpectralArrow(lvl, spectralArrow);
                }
            },
            weapon
        );
    }

    public static void onLivingDeath(LivingEntity target, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            runIterationOnLiving(
                (slot, itemStack, ench, lvl) -> {
                    if (ench instanceof ILivingEnchantment.Death _ench) {
                        _ench.onLivingDeath(lvl, target, source);
                    }
                },
                sourceLiving
            );
        }
    }

    public static float onLivingHurt(LivingEntity target, DamageSource source, float originalAmount) {
        var amount = new MutableFloat(originalAmount);
        runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.Hurt _ench) {
                    amount.setValue(_ench.onLivingHurt(lvl, false, slot, target, source, amount.floatValue()));
                }
            },
            target
        );
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            runIterationOnLiving(
                (slot, itemStack, ench, lvl) -> {
                    if (ench instanceof ILivingEnchantment.Hurt _ench) {
                        amount.setValue(_ench.onLivingHurt(lvl, true, slot, target, source, amount.floatValue()));
                    }
                },
                sourceLiving
            );
        }
        return amount.floatValue();
    }

    public static int onItemHurt(ItemStack itemStack, int originalDamage, Random random, ServerPlayer player) {
        if (NOT_ITEM.test(itemStack)) return originalDamage;
        var damage = new MutableInt(originalDamage);
        runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IItemEnchantment.Hurt _ench) {
                    damage.setValue(_ench.onItemHurt(lvl, itemStack, damage.intValue(), random, player));
                }
            },
            itemStack
        );
        return damage.intValue();
    }

    public static int getDamageProtectionWithItem(Iterable<ItemStack> items, DamageSource source) {
        MutableInt protection = new MutableInt();
        runIterationOnItems(
            (itemStack, ench, lvl) -> {
                if (ench instanceof IItemEnchantment.Protection _ench) {
                    protection.add(_ench.getDamageProtection(lvl, itemStack, source));
                }
            },
            items, NOT_BOOK
        );
        return protection.intValue();
    }

    public static void onSelectedItemTick(ItemStack itemStack, Player player, Level level) {
        if (IS_ITEM.test(itemStack)) {
            runIterationOnItem(
                (ench, lvl) -> {
                    if (ench instanceof IItemEnchantment.Tick.Selected _ench) {
                        _ench.onSelectedTick(lvl, itemStack, player, level);
                    }
                },
                itemStack
            );
        }
    }

    public static Pair<Integer, Integer> preFishingRodCast(ItemStack fishingRod, ServerPlayer player, Level level, int originalSpeedBonus, int originalLuckBonus) {
        var bonus = new MutablePair<Integer, Integer>(originalSpeedBonus, originalLuckBonus);
        runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IItemEnchantment.FishingRod _ench) {
                    var r = _ench.preFishingRodCast(lvl, fishingRod, player, level, bonus.getLeft(), bonus.getRight());
                    bonus.setLeft(r.getLeft());
                    bonus.setRight(r.getRight());
                }
            },
            fishingRod
        );
        return bonus;
    }

    public static Pair<Integer, Float> onPlayerEat(ServerPlayer player, ItemStack foodItemStack, int originalNutrition, float originalSaturationModifier) {
        var foodValues = new MutablePair<Integer, Float>(originalNutrition, originalSaturationModifier);
        runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof IPlayerEnchantment.Eat _ench) {
                    var r = _ench.onPlayerEat(lvl, itemStack, slot, player, foodItemStack, foodValues.getLeft(), foodValues.getRight());
                    foodValues.setLeft(r.getLeft());
                    foodValues.setRight(r.getRight());
                }
            },
            player
        );
        return foodValues;
    }

    public static PiglinStanceEvent.Stance onPiglinChooseStance(LivingEntity target, PiglinStanceEvent.Stance originalStance) {
        var stance = new MutableObject<PiglinStanceEvent.Stance>(originalStance);
        runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.PiglinStance _ench) {
                    stance.setValue(_ench.onPiglinChooseStance(lvl, slot, target, stance.getValue()));
                }
            },
            target
        );
        return stance.getValue();
    }

    public static void onPotionAdded(LivingEntity target, @Nullable Entity source, MobEffectInstance newEffect, @Nullable MobEffectInstance oldEffect) {
        runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.Potion _ench) {
                    _ench.onPotionAdded(lvl, slot, source, newEffect, oldEffect);
                }
            },
            target
        );
    }

}
