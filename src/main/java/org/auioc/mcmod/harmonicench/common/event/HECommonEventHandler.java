package org.auioc.mcmod.harmonicench.common.event;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.auioc.mcmod.arnicalib.game.entity.projectile.IHProjectile;
import org.auioc.mcmod.arnicalib.game.event.common.ItemInventoryTickEvent;
import org.auioc.mcmod.arnicalib.game.event.common.PlayerEatEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;


public class HECommonEventHandler {

    @SubscribeEvent
    public static void onGetItemAttributeModifier(final ItemAttributeModifierEvent event) {
        EnchantmentPerformer.getAttributeModifiers(event.getItemStack(), event.getSlotType())
            .ifPresent((modifierList) -> {
                modifierList.forEach(
                    (modifiers) -> modifiers.forEach(event::addModifier)
                );
            });
    }

    @SubscribeEvent
    public static void onLivingHurt(final LivingHurtEvent event) {
        var damageSource = event.getSource();
        var target = event.getEntity();

        if (damageSource.getEntity() instanceof LivingEntity owner) {
            if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && damageSource.getDirectEntity() instanceof Projectile projectile) {
                event.setAmount(EnchantmentPerformer.onProjectileHurtLiving(target, projectile, owner, ((IHProjectile) projectile).getShootingPosition(), event.getAmount()));
            }
            if (damageSource.is(DamageTypeTags.IS_EXPLOSION) && damageSource.getDirectEntity() instanceof FireworkRocketEntity fireworkRocket) {
                event.setAmount(EnchantmentPerformer.onFireworkRocketExplode(target, fireworkRocket, owner, event.getAmount()));
            }
        }

        event.setAmount(EnchantmentPerformer.onLivingHurt(target, damageSource, event.getAmount()));
    }

    @SubscribeEvent
    public static void onLivingDeath(final LivingDeathEvent event) {
        EnchantmentPerformer.onLivingDeath(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void onItemInventoryTick(final ItemInventoryTickEvent event) {
        EnchantmentPerformer.onItemInventoryTick(event.getItemStack(), event.getEntity(), event.getLevel(), event.getIndex(), event.isSelected());
    }

    @SubscribeEvent
    public static void onLivingEat(final PlayerEatEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var r = EnchantmentPerformer.onPlayerEat(player, event.getFood(), event.getNutrition(), event.getSaturationModifier());
            int nutrition = r.getLeft();
            float saturationModifier = r.getRight();
            if (nutrition == 0) {
                event.setCanceled(true);
                player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
            } else {
                event.setNutrition(nutrition);
                event.setSaturationModifier(saturationModifier);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerGetBreakSpeed(final PlayerEvent.BreakSpeed event) {
        float r = EnchantmentPerformer.getBreakSpeed(event.getEntity(), event.getState(), event.getPosition(), event.getOriginalSpeed());
        event.setNewSpeed(r);
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        EnchantmentPerformer.onPlayerTick(event.player, event.phase, event.side);
    }

    @SubscribeEvent
    public static void onCriticalHit(final CriticalHitEvent event) {
        event.setDamageModifier(EnchantmentPerformer.onCriticalHit(event.getEntity(), event.getTarget(), event.getDamageModifier()));
    }

}
