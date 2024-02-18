package org.auioc.mcmod.harmonicench.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.harmonicench.client.event.HEClientEventHandler;

@OnlyIn(Dist.CLIENT)
public class ClientInitialization {

    public static void init() {
        forgeSetup();
    }

    // private static final IEventBus modEventBus = HarmonicEnchantments.getModEventBus();
    private static final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

    private static void forgeSetup() {
        forgeEventBus.register(HEClientEventHandler.class);
    }


}
