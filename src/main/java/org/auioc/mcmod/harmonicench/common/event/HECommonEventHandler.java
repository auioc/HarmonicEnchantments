package org.auioc.mcmod.harmonicench.common.event;

import org.auioc.mcmod.arnicalib.common.event.impl.ItemInventoryTickEvent;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinProjectile;
import org.auioc.mcmod.harmonicench.common.event.impl.LivingEatEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

        if (source.isProjectile()) {
            if (
                source.getDirectEntity() instanceof Projectile projectile
                    && source.getEntity() instanceof LivingEntity owner
            ) {
                event.setAmount(EnchantmentHelper.onProjectileHurtLiving(target, projectile, owner, ((IMixinProjectile) projectile).getShootingPosition(), amount));
            }
        } else {
            event.setAmount(EnchantmentHelper.onLivingHurt(target, source, amount));
        }
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
        var living = event.getEntityLiving();
        if (living != null && !living.level.isClientSide) {
            var r = EnchantmentHelper.onLivingEat(living, event.getFoodItemStack(), event.getNutrition(), event.getSaturationModifier());
            int nutrition = r.getLeft();
            float saturationModifier = r.getRight();
            if (nutrition == 0 && saturationModifier == 0.0F) {
                event.setCanceled(true);
            } else {
                event.setNutrition(nutrition);
                event.setSaturationModifier(saturationModifier);
            }
        }
    }

}
