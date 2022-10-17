package org.auioc.mcmod.harmonicench.server.event;

import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.server.event.impl.ApplyLootEnchantmentBonusCountEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import org.auioc.mcmod.hulsealib.game.event.server.CatMorningGiftChanceEvent;
import org.auioc.mcmod.hulsealib.game.event.server.ItemHurtEvent;
import org.auioc.mcmod.hulsealib.game.event.server.PiglinStanceEvent;
import org.auioc.mcmod.hulsealib.game.event.server.PreBowReleaseEvent;
import org.auioc.mcmod.hulsealib.game.event.server.PreCrossbowReleaseEvent;
import org.auioc.mcmod.hulsealib.game.event.server.PreFishingRodCastEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HEServerEventHandler {

    @SubscribeEvent
    public static void onItemHurt(final ItemHurtEvent event) {
        var player = event.getPlayer();
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
    public static void preFishingRodCast(final PreFishingRodCastEvent event) {
        var r = EnchantmentPerformer.preFishingRodCast(event.getFishingRod(), event.getPlayer(), event.getSpeedBonus(), event.getLuckBonus());
        event.setSpeedBonus(r.getLeft());
        event.setLuckBonus(r.getRight());
    }

    @SubscribeEvent
    public static void preBowRelease(final PreBowReleaseEvent event) {
        EnchantmentHelper.copyItemEnchantmentsToEntity(event.getBow(), event.getArrow());
        EnchantmentPerformer.handleProjectile(event.getBow(), event.getArrow());
    }

    @SubscribeEvent
    public static void preCrossbowRelease(final PreCrossbowReleaseEvent event) {
        EnchantmentHelper.copyItemEnchantmentsToEntity(event.getBow(), event.getProjectile());
        EnchantmentPerformer.handleProjectile(event.getBow(), event.getProjectile());
    }

    @SubscribeEvent
    public static void onPiglinChooseStance(final PiglinStanceEvent event) {
        var r = EnchantmentPerformer.onPiglinChooseStance(event.getTarget(), event.getStance());
        event.setStance(r);
    }

    @SubscribeEvent
    public static void onPotionAdded(final PotionAddedEvent event) {
        EnchantmentPerformer.onPotionAdded(event.getEntityLiving(), event.getPotionSource(), event.getPotionEffect(), event.getOldPotionEffect());
    }

    @SubscribeEvent
    public static void onSetCatMorningGiftChance(final CatMorningGiftChanceEvent event) {
        var r = EnchantmentPerformer.onSetCatMorningGiftChance(event.getCat(), event.getOwnerPlayer(), event.getChance());
        event.setChance(r);
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
