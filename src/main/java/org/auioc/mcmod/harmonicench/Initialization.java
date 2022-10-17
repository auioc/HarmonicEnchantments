package org.auioc.mcmod.harmonicench;

import org.auioc.mcmod.harmonicench.common.config.HECommonConfig;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.event.HECommonEventHandler;
import org.auioc.mcmod.harmonicench.common.itemgroup.HECreativeModeTabs;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import org.auioc.mcmod.harmonicench.server.event.HEServerEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
public final class Initialization {

    private Initialization() {}

    public static void init() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        final ClientSideOnlySetup ClientSideOnlySetup = new ClientSideOnlySetup(modEventBus, forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::registerConfig);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::modSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::forgeSetup);

        final CommonSetup CommonSetup = new CommonSetup(modEventBus, forgeEventBus);
        CommonSetup.modSetup();
        CommonSetup.forgeSetup();
        CommonSetup.registerConfig();
    }


    private final static class CommonSetup {

        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public CommonSetup(final IEventBus modEventBus, final IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void registerConfig() {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HECommonConfig.CONFIG);
        }

        private void modSetup() {
            HEEnchantments.ENCHANTMENTS.register(modEventBus);
            HEMobEffects.MOB_EFFECTS.register(modEventBus);
        }

        private void forgeSetup() {
            HECreativeModeTabs.init();
            forgeEventBus.register(HECommonEventHandler.class);
            forgeEventBus.register(HEServerEventHandler.class);
        }

    }


    private final static class ClientSideOnlySetup {

        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public ClientSideOnlySetup(final IEventBus modEventBus, final IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void registerConfig() {}

        public void modSetup() {}

        public void forgeSetup() {}

    }

}
