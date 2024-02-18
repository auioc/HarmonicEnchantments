package org.auioc.mcmod.harmonicench.client.event;

import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.auioc.mcmod.harmonicench.api.effect.IMovementInputMobEffect;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;

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
