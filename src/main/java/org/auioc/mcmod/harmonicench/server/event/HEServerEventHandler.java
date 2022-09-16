package org.auioc.mcmod.harmonicench.server.event;

import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.server.event.impl.CatMorningGiftChanceEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.FishingRodCastEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.ItemHurtEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.PiglinStanceEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HEServerEventHandler {

    @SubscribeEvent
    public static void onItemHurt(final ItemHurtEvent event) {
        var player = event.getPlayer();
        if (player == null) return;

        event.setDamage(EnchantmentHelper.onItemHurt(event.getItemStack(), event.getDamage(), event.getRandom(), player));
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
    public static void onPreFishingRodCast(final FishingRodCastEvent.Pre event) {
        var r = EnchantmentHelper.preFishingRodCast(event.getFishingRod(), event.getPlayer(), event.getLevel(), event.getSpeedBonus(), event.getLuckBonus());
        event.setSpeedBonus(r.getLeft());
        event.setLuckBonus(r.getRight());
    }

    @SubscribeEvent
    public static void onPiglinChooseStance(final PiglinStanceEvent event) {
        var r = EnchantmentHelper.onPiglinChooseStance(event.getTarget(), event.getStance());
        event.setStance(r);
    }

    @SubscribeEvent
    public static void onPotionAdded(final PotionAddedEvent event) {
        EnchantmentHelper.onPotionAdded(event.getEntityLiving(), event.getPotionSource(), event.getPotionEffect(), event.getOldPotionEffect());
    }

    @SubscribeEvent
    public static void onSetCatMorningGiftChance(final CatMorningGiftChanceEvent event) {
        var r = EnchantmentHelper.onSetCatMorningGiftChance(event.getCat(), event.getOwnerPlayer(), event.getChance());
        event.setChance(r);
    }

}
