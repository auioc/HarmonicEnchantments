package org.auioc.mcmod.harmonicench.client.event;

import org.auioc.mcmod.harmonicench.api.effect.IMovementInputMobEffect;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public final class HEClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        EnchantmentPerformer.onItemTooltip(event.getItemStack(), event.getEntity(), event.getToolTip(), event.getFlags());
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        var input = event.getInput();
        var player = (LocalPlayer) event.getEntity();
        for (var entry : player.getActiveEffectsMap().entrySet()) {
            if (entry.getKey() instanceof IMovementInputMobEffect effect) {
                effect.onMovementInputUpdate(entry.getValue().getAmplifier(), input, player);
            }
        }
    }

}
