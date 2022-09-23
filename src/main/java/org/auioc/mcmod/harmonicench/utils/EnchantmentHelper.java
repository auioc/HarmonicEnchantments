package org.auioc.mcmod.harmonicench.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.arnicalib.api.game.enchantment.IEnchantmentAttachableObject;
import org.auioc.mcmod.arnicalib.api.mixin.common.IMixinArrow;
import org.auioc.mcmod.arnicalib.server.event.impl.PiglinStanceEvent;
import org.auioc.mcmod.arnicalib.utils.game.EnchUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILootBonusEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Cat;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {

    public static final Predicate<ItemStack> NOT_BOOK = (itemStack) -> !itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> NOT_ITEM = (itemStack) -> itemStack.isEmpty() || itemStack.is(Items.ENCHANTED_BOOK);
    public static final Predicate<ItemStack> IS_ITEM = (itemStack) -> !itemStack.isEmpty() && !itemStack.is(Items.ENCHANTED_BOOK);


    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (NOT_ITEM.test(itemStack)) return Optional.empty();
        var modifiers = new ArrayList<Map<Attribute, AttributeModifier>>();
        EnchUtils.runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IAttributeModifierEnchantment _ench) {
                    _ench.getOptionalAttributeModifier(lvl, slotType, itemStack).ifPresent((m) -> modifiers.add(m));
                }
            },
            itemStack
        );
        return Optional.of(modifiers);
    }

    public static boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {
        if (NOT_ITEM.test(itemStack)) return true;
        var bool = new MutableBoolean(true);
        EnchUtils.runIterationOnItem(
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
        if (entity instanceof IEnchantmentAttachableObject _entity) {
            for (var enchEntry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                if (predicate.test(enchEntry.getKey(), enchEntry.getValue())) {
                    _entity.addEnchantment(enchEntry.getKey(), enchEntry.getValue());
                }
            }
        }
    }

    public static float onProjectileHurtLiving(LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 shootingPosition, float originalAmount) {
        var enchMap = ((IEnchantmentAttachableObject) projectile).getEnchantments();
        if (enchMap == null) return originalAmount;

        var amount = new MutableFloat(originalAmount);
        EnchUtils.runIteration(
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
        EnchUtils.runIterationOnItem(
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
            EnchUtils.runIterationOnLiving(
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
        EnchUtils.runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.Hurt _ench) {
                    amount.setValue(_ench.onLivingHurt(lvl, false, slot, target, source, amount.floatValue()));
                }
            },
            target
        );
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            EnchUtils.runIterationOnLiving(
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
        EnchUtils.runIterationOnItem(
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
        EnchUtils.runIterationOnItems(
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
            EnchUtils.runIterationOnItem(
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
        EnchUtils.runIterationOnItem(
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
        EnchUtils.runIterationOnLiving(
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
        EnchUtils.runIterationOnLiving(
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
        EnchUtils.runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.Potion _ench) {
                    _ench.onPotionAdded(lvl, slot, source, newEffect, oldEffect);
                }
            },
            target
        );
    }

    public static double onSetCatMorningGiftChance(Cat cat, Player ownerPlayer, double originalChance) {
        var chance = new MutableDouble(originalChance);
        EnchUtils.runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof ILivingEnchantment.Cat _ench) {
                    chance.setValue(_ench.onSetCatMorningGiftChance(lvl, slot, cat, ownerPlayer, chance.doubleValue()));
                }
            },
            ownerPlayer
        );
        return chance.doubleValue();
    }

    public static int onApplyLootEnchantmentBonusCount(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int originalEnchantmentLevel) {
        var enchantmentLevel = new MutableInt(originalEnchantmentLevel);
        EnchUtils.runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof ILootBonusEnchantment.ApplyBonusCountFunction _ench) {
                    enchantmentLevel.setValue(_ench.onApplyLootEnchantmentBonusCount(lvl, lootContext, itemStack, enchantment, enchantmentLevel.intValue()));
                }
            },
            itemStack
        );
        return enchantmentLevel.intValue();
    }

    public static void onPlayerServerTick(ServerPlayer player) {
        EnchUtils.runIterationOnLiving(
            (slot, itemStack, ench, lvl) -> {
                if (ench instanceof IPlayerEnchantment.Tick.Server _ench) {
                    _ench.onPlayerServerTick(lvl, itemStack, slot, player);
                }
            },
            player
        );
    }

    public static boolean canElytraFly(ItemStack itemStack, LivingEntity living) {
        var bool = new MutableBoolean(true);
        EnchUtils.runIterationOnItem(
            (ench, lvl) -> {
                if (ench instanceof IItemEnchantment.Elytra _ench) {
                    bool.setValue(bool.booleanValue() & _ench.canElytraFly(lvl, itemStack, living));
                }
            },
            itemStack
        );
        return bool.booleanValue();
    }

}
