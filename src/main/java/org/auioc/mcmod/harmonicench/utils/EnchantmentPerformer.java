package org.auioc.mcmod.harmonicench.utils;

import static org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentIterator.run;
import static org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentIterator.runOnItem;
import static org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentIterator.runOnItems;
import static org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentIterator.runOnLiving;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.arnicalib.game.enchantment.IEnchantmentAttachableObject;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;
import org.auioc.mcmod.arnicalib.mod.mixin.common.MixinAccessorArrow;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IBlockEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IConfigurableEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILootBonusEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class EnchantmentPerformer {

    public static boolean isEnabled(Enchantment ench) {
        if (ench instanceof IConfigurableEnchantment hench) {
            return hench.isEnabled();
        }
        return true;
    }

    public static <T> boolean perform(Enchantment ench, Class<T> clazz, Consumer<T> action) {
        if (clazz.isInstance(ench)) {
            if (isEnabled(ench)) {
                action.accept(clazz.cast(ench));
                return true;
            }
        }
        return false;
    }

    public static Optional<List<Map<Attribute, AttributeModifier>>> getAttributeModifiers(ItemStack itemStack, EquipmentSlot slotType) {
        if (EnchantmentHelper.NOT_ITEM.test(itemStack)) return Optional.empty();
        var modifiers = new ArrayList<Map<Attribute, AttributeModifier>>();
        runOnItem(
            (ench, lvl) -> perform(
                ench, IAttributeModifierEnchantment.class,
                (e) -> e.getOptionalAttributeModifier(lvl, slotType, itemStack).ifPresent((m) -> modifiers.add(m))
            ),
            itemStack
        );
        return Optional.of(modifiers);
    }

    public static boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {
        if (EnchantmentHelper.NOT_ITEM.test(itemStack)) return true;
        var bool = new MutableBoolean(true);
        runOnItem(
            (ench, lvl) -> perform(
                ench, IToolActionControllerEnchantment.class,
                (e) -> bool.setValue(bool.booleanValue() & e.canPerformAction(toolAction))
            ),
            itemStack
        );
        return bool.booleanValue();
    }

    public static float onProjectileHurtLiving(LivingEntity target, Projectile projectile, LivingEntity owner, Vec3 shootingPosition, float originalAmount) {
        var enchMap = ((IEnchantmentAttachableObject) projectile).getEnchantments();
        if (enchMap == null) return originalAmount;

        var amount = new MutableFloat(originalAmount);
        run(
            (ench, lvl) -> perform(
                ench, IProjectileEnchantment.HurtLiving.class,
                (e) -> amount.setValue(e.onHurtLiving(lvl, target, projectile, owner, shootingPosition, amount.floatValue()))
            ),
            enchMap
        );
        return amount.floatValue();
    }

    public static float onFireworkRocketExplode(LivingEntity target, FireworkRocketEntity fireworkRocket, LivingEntity owner, float originalAmount) {
        var enchMap = ((IEnchantmentAttachableObject) fireworkRocket).getEnchantments();
        if (enchMap == null) return originalAmount;

        var amount = new MutableFloat(originalAmount);
        run(
            (ench, lvl) -> perform(
                ench, IProjectileEnchantment.FireworkRocket.class,
                (e) -> amount.setValue(e.onFireworkRocketExplode(lvl, target, fireworkRocket, owner, amount.floatValue()))
            ),
            enchMap
        );
        return amount.floatValue();
    }

    public static void handleProjectile(ItemStack weapon, Projectile projectile) {
        runOnItem(
            (ench, lvl) -> {
                if (projectile instanceof AbstractArrow abstractArrow) {
                    perform(ench, IProjectileEnchantment.AbstractArrow.class, (e) -> e.handleAbstractArrow(lvl, abstractArrow));
                    if (ench instanceof IProjectileEnchantment.TippedArrow _ench && isEnabled(ench) && abstractArrow instanceof Arrow arrow) {
                        var potionArrow = (MixinAccessorArrow) arrow;
                        if (potionArrow.getPotion() != Potions.EMPTY || !potionArrow.getEffects().isEmpty()) {
                            _ench.handleTippedArrow(lvl, arrow, potionArrow);
                        }
                    }
                    if (ench instanceof IProjectileEnchantment.SpectralArrow _ench && isEnabled(ench) && abstractArrow instanceof SpectralArrow spectralArrow) {
                        _ench.handleSpectralArrow(lvl, spectralArrow);
                    }
                } else {
                    if (ench instanceof IProjectileEnchantment.FireworkRocket _ench && isEnabled(ench) && projectile instanceof FireworkRocketEntity fireworkRocket) {
                        _ench.handleFireworkRocket(lvl, fireworkRocket);
                    }
                }
            },
            weapon
        );
    }

    public static float onLivingHurt(LivingEntity target, DamageSource source, float originalAmount) {
        var amount = new MutableFloat(originalAmount);
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, ILivingEnchantment.Hurt.class,
                (e) -> amount.setValue(e.onLivingHurt(lvl, false, slot, target, source, amount.floatValue()))
            ),
            target
        );
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            runOnLiving(
                (slot, itemStack, ench, lvl) -> perform(
                    ench, ILivingEnchantment.Hurt.class,
                    (e) -> amount.setValue(e.onLivingHurt(lvl, true, slot, target, source, amount.floatValue()))
                ),
                sourceLiving
            );
        }
        return amount.floatValue();
    }

    public static int onItemHurt(ItemStack itemStack, int originalDamage, RandomSource random, ServerPlayer player) {
        if (EnchantmentHelper.NOT_ITEM.test(itemStack)) return originalDamage;
        var damage = new MutableInt(originalDamage);
        runOnItem(
            (ench, lvl) -> perform(
                ench, IItemEnchantment.Hurt.class,
                (e) -> damage.setValue(e.onItemHurt(lvl, itemStack, damage.intValue(), random, player))
            ),
            itemStack
        );
        return damage.intValue();
    }

    /**
     * FMLCoreMod: harmonicench.living_entity.magic_absorb
     */
    public static int getDamageProtectionWithItem(Iterable<ItemStack> items, DamageSource source) {
        MutableInt protection = new MutableInt();
        runOnItems(
            (itemStack, ench, lvl) -> perform(
                ench, IItemEnchantment.Protection.class,
                (e) -> protection.add(e.getDamageProtection(lvl, itemStack, source))
            ),
            items, EnchantmentHelper.NOT_BOOK
        );
        return protection.intValue();
    }

    public static void onItemInventoryTick(ItemStack itemStack, Player player, Level level, int index, boolean selected) {
        if (EnchantmentHelper.IS_ITEM.test(itemStack)) {
            runOnItem(
                (ench, lvl) -> perform(
                    ench, IItemEnchantment.Tick.Inventory.class,
                    (e) -> e.onInventoryTick(lvl, itemStack, player, level, index, selected)
                ),
                itemStack
            );
        }
    }

    public static Pair<Integer, Integer> preFishingRodCast(ItemStack fishingRod, ServerPlayer player, int originalSpeedBonus, int originalLuckBonus) {
        var bonus = new MutablePair<Integer, Integer>(originalSpeedBonus, originalLuckBonus);
        runOnItem(
            (ench, lvl) -> perform(
                ench, IItemEnchantment.FishingRod.class,
                (e) -> {
                    var r = e.preFishingRodCast(lvl, fishingRod, player, bonus.getLeft(), bonus.getRight());
                    bonus.setLeft(r.getLeft());
                    bonus.setRight(r.getRight());
                }
            ),
            fishingRod
        );
        return bonus;
    }

    public static Pair<Integer, Float> onPlayerEat(ServerPlayer player, ItemStack foodItemStack, int originalNutrition, float originalSaturationModifier) {
        var foodValues = new MutablePair<Integer, Float>(originalNutrition, originalSaturationModifier);
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, IPlayerEnchantment.Eat.class,
                (e) -> {
                    var r = e.onPlayerEat(lvl, itemStack, slot, player, foodItemStack, foodValues.getLeft(), foodValues.getRight());
                    foodValues.setLeft(r.getLeft());
                    foodValues.setRight(r.getRight());
                }
            ),
            player
        );
        return foodValues;
    }

    public static MobStance onPiglinChooseStance(LivingEntity target, MobStance originalStance) {
        var stance = new MutableObject<MobStance>(originalStance);
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, ILivingEnchantment.PiglinStance.class,
                (e) -> stance.setValue(e.onPiglinChooseStance(lvl, slot, target, stance.getValue()))
            ),
            target
        );
        return stance.getValue();
    }

    public static void onMobEffectAdded(LivingEntity target, @Nullable Entity source, MobEffectInstance newEffect, @Nullable MobEffectInstance oldEffect) {
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, ILivingEnchantment.Potion.class,
                (e) -> e.onPotionAdded(lvl, slot, source, newEffect, oldEffect)
            ),
            target
        );
    }

    public static void onCatMorningGiftConditionCheck(Cat cat, Player owner, LinkedHashMap<ResourceLocation, BiPredicate<Cat, Player>> conditions) {
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, ILivingEnchantment.Cat.class,
                (e) -> e.onCatMorningGiftConditionCheck(lvl, slot, cat, owner, conditions)
            ),
            owner
        );
    }

    public static int onApplyLootEnchantmentBonusCount(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int originalEnchantmentLevel) {
        var enchantmentLevel = new MutableInt(originalEnchantmentLevel);
        runOnItem(
            (ench, lvl) -> perform(
                ench, ILootBonusEnchantment.ApplyBonusCountFunction.class,
                (e) -> enchantmentLevel.setValue(e.onApplyLootEnchantmentBonusCount(lvl, lootContext, itemStack, enchantment, enchantmentLevel.intValue()))
            ),
            itemStack
        );
        return enchantmentLevel.intValue();
    }

    public static void onPlayerTick(Player player, TickEvent.Phase phase, LogicalSide side) {
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, IPlayerEnchantment.Tick.class,
                (e) -> e.onPlayerTick(lvl, itemStack, slot, player, phase, side)
            ),
            player
        );
    }

    public static boolean canElytraFly(ItemStack itemStack, LivingEntity living) {
        var bool = new MutableBoolean(true);
        runOnItem(
            (ench, lvl) -> perform(
                ench, IItemEnchantment.Elytra.class,
                (e) -> bool.setValue(bool.booleanValue() & e.canElytraFly(lvl, itemStack, living))
            ),
            itemStack
        );
        return bool.booleanValue();
    }

    public static float getBreakSpeed(Player player, BlockState blockState, Optional<BlockPos> blockPos, float originalSpeed) {
        var speed = new MutableFloat(originalSpeed);
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, IBlockEnchantment.BreakSpeed.class,
                (e) -> speed.setValue(e.getBreakSpeed(lvl, itemStack, player, blockState, blockPos, speed.floatValue()))
            ),
            player
        );
        return speed.floatValue();
    }

    public static void onBlockBreak(Player player, BlockState blockState, BlockPos blockPos) {
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, IBlockEnchantment.Break.class,
                (e) -> e.onBlockBreak(lvl, itemStack, player, blockState, blockPos)
            ),
            player
        );
    }

    public static void onLivingDeath(LivingEntity target, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity sourceLiving) {
            runOnLiving(
                (slot, itemStack, ench, lvl) -> perform(
                    ench, ILivingEnchantment.Death.class,
                    (e) -> e.onLivingDeath(lvl, itemStack, target, source)
                ),
                sourceLiving
            );
        }
    }

    public static float onCriticalHit(Player player, Entity target, float originalDamageModifier) {
        var damageModifier = new MutableFloat(originalDamageModifier);
        runOnLiving(
            (slot, itemStack, ench, lvl) -> perform(
                ench, IPlayerEnchantment.CriticalHit.class,
                (e) -> damageModifier.setValue(e.onCriticalHit(lvl, itemStack, player, target, damageModifier.floatValue()))
            ),
            player
        );
        return damageModifier.floatValue();
    }

    @OnlyIn(Dist.CLIENT)
    public static void onItemTooltip(@NotNull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        runOnItem((ench, lvl) -> perform(ench, IItemEnchantment.Tooltip.class, (e) -> e.onItemTooltip(lvl, itemStack, player, lines, flags)), itemStack);
    }

}
