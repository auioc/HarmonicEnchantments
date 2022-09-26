package org.auioc.mcmod.harmonicench.common.event;

import org.auioc.mcmod.arnicalib.api.mixin.common.IMixinProjectile;
import org.auioc.mcmod.arnicalib.common.event.impl.ItemInventoryTickEvent;
import org.auioc.mcmod.arnicalib.common.event.impl.LivingEatEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HECommonEventHandler {

    @SubscribeEvent
    public static void onGetItemAttributeModifier(final ItemAttributeModifierEvent event) {
        EnchantmentHelper.getAttributeModifiers(event.getItemStack(), event.getSlotType())
            .ifPresent((modifierList) -> {
                modifierList.forEach((modifiers) -> {
                    modifiers.forEach((attribute, modifier) -> {
                        event.addModifier(attribute, modifier);
                    });
                });
            });
    }

    @SubscribeEvent
    public static void onLivingHurt(final LivingHurtEvent event) {
        var source = event.getSource();
        var target = event.getEntityLiving();
        float amount = event.getAmount();

        if (source instanceof IndirectEntityDamageSource indirectSource && indirectSource.getEntity() instanceof LivingEntity owner) {
            if (indirectSource.isProjectile() && indirectSource.getDirectEntity() instanceof Projectile projectile) {
                event.setAmount(EnchantmentHelper.onProjectileHurtLiving(target, projectile, owner, ((IMixinProjectile) projectile).getShootingPosition(), amount));
            }
            if (indirectSource.isExplosion() && indirectSource.getDirectEntity() instanceof FireworkRocketEntity fireworkRocket) {
                event.setAmount(EnchantmentHelper.onFireworkRocketExplode(target, fireworkRocket, owner, amount));
            }
        }
        event.setAmount(EnchantmentHelper.onLivingHurt(target, source, amount));

    }

    @SubscribeEvent
    public static void onLivingDeath(final LivingDeathEvent event) {
        EnchantmentHelper.onLivingDeath(event.getEntityLiving(), event.getSource());
    }

    @SubscribeEvent
    public static void onSelectedItemTick(final ItemInventoryTickEvent.Selected event) {
        EnchantmentHelper.onSelectedItemTick(event.getItemStack(), event.getPlayer(), event.getLevel());
    }

    @SubscribeEvent
    public static void onLivingEat(final LivingEatEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            var r = EnchantmentHelper.onPlayerEat(player, event.getFoodItemStack(), event.getNutrition(), event.getSaturationModifier());
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
        float r = EnchantmentHelper.getBreakSpeed(event.getPlayer(), event.getState(), event.getPos(), event.getOriginalSpeed());
        event.setNewSpeed(r);
    }

}
