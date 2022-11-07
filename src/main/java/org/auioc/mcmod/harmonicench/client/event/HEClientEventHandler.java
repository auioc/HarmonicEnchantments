package org.auioc.mcmod.harmonicench.client.event;

import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public final class HEClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        EnchantmentPerformer.onItemTooltip(event.getItemStack(), event.getPlayer(), event.getToolTip(), event.getFlags());
    }

}
