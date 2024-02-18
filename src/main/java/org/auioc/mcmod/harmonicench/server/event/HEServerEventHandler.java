package org.auioc.mcmod.harmonicench.server.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.auioc.mcmod.arnicalib.game.event.server.FishingRodCastEvent;
import org.auioc.mcmod.arnicalib.game.event.server.ItemHurtEvent;
import org.auioc.mcmod.arnicalib.game.event.server.ProjectileWeaponReleaseEvent;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.server.event.impl.ApplyLootEnchantmentBonusCountEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.CatMorningGiftEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;

public final class HEServerEventHandler {

    @SubscribeEvent
    public static void onItemHurt(final ItemHurtEvent event) {
        var player = event.getServerPlayer();
        if (player == null) return;

        event.setDamage(EnchantmentPerformer.onItemHurt(event.getItemStack(), event.getDamage(), event.getRandom(), player));
    }

    @SubscribeEvent
    public static void onEnderPearlTeleport(final EntityTeleportEvent.EnderPearl event) {
        if (EnchantmentHelper.getEnchantmentLevel(HEEnchantments.SAFE_TELEPORTING.get(), event.getPlayer()) > 0) {
            event.setAttackDamage(0.0F);
        }
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(final EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            {
                int lvl = EnchantmentHelper.getEnchantmentLevel(HEEnchantments.SAFE_TELEPORTING.get(), living);
                if (lvl > 0) SafeTeleportingEnchantment.handleLivingTravelToDimension(lvl, living);
            }
        }
    }

    @SubscribeEvent
    public static void preFishingRodCast(final FishingRodCastEvent event) {
        var r = EnchantmentPerformer.preFishingRodCast(event.getFishingRod(), event.getServerPlayer(), event.getSpeedBonus(), event.getLuckBonus());
        event.setSpeedBonus(r.getLeft());
        event.setLuckBonus(r.getRight());
    }

    @SubscribeEvent
    public static void preProjectileWeaponRelease(final ProjectileWeaponReleaseEvent event) {
        EnchantmentHelper.copyItemEnchantmentsToEntity(event.getWeapon(), event.getProjectile());
        EnchantmentPerformer.handleProjectile(event.getWeapon(), event.getProjectile());
    }

    @SubscribeEvent
    public static void onMobEffectAdded(final MobEffectEvent.Added event) {
        EnchantmentPerformer.onMobEffectAdded(event.getEntity(), event.getEffectSource(), event.getEffectInstance(), event.getOldEffectInstance());
    }

    @SubscribeEvent
    public static void onCatMorningGiftConditionCheck(final CatMorningGiftEvent.Check event) {
        EnchantmentPerformer.onCatMorningGiftConditionCheck(event.getCat(), event.getOwner(), event.getConditions());
    }

    @SubscribeEvent
    public static void onApplyLootEnchantmentBonusCount(final ApplyLootEnchantmentBonusCountEvent event) {
        var r = EnchantmentPerformer.onApplyLootEnchantmentBonusCount(event.getLootContext(), event.getItemStack(), event.getEnchantment(), event.getEnchantmentLevel());
        event.setEnchantmentLevel(r);
    }

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        EnchantmentPerformer.onBlockBreak(event.getPlayer(), event.getState(), event.getPos());
    }

}
