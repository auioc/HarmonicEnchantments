package org.auioc.mcmod.harmonicench.common.event;

import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.ItemAttributeModifierEvent;
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
        if (source.isProjectile()) {
            if (source.getDirectEntity() instanceof Projectile projectile && source.getEntity() instanceof LivingEntity owner) {
                event.setAmount(EnchantmentHelper.onProjectileHitLiving(target, projectile, owner, source.getSourcePosition(), event.getAmount()));
            }
        }
    }

}
