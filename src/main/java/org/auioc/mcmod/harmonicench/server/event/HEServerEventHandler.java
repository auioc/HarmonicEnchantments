package org.auioc.mcmod.harmonicench.server.event;

import org.auioc.mcmod.harmonicench.server.event.impl.ItemHurtEvent;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HEServerEventHandler {

    @SubscribeEvent
    public static void onItemHurt(final ItemHurtEvent event) {
        var player = event.getPlayer();
        if (player == null) return;

        event.setDamage(EnchantmentHelper.onItemHurt(event.getItemStack(), event.getDamage(), event.getRandom(), player));
    }

}
