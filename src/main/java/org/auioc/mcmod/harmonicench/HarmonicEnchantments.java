package org.auioc.mcmod.harmonicench;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.Logger;
import org.auioc.mcmod.arnicalib.base.log.LogUtil;
import org.auioc.mcmod.arnicalib.game.mod.BuildInfo;
import org.auioc.mcmod.arnicalib.game.mod.IHMod;


@Mod(HarmonicEnchantments.MOD_ID)
public final class HarmonicEnchantments implements IHMod {

    public static final String MOD_ID = "harmonicench";
    public static final String MOD_NAME = "HarmonicEnchantments";
    public static final Logger LOGGER = LogUtil.getLogger(MOD_NAME);
    public static final BuildInfo BUILD_INFO = BuildInfo.fromPackage(HarmonicEnchantments.class);

    private static IEventBus modEventBus;

    public HarmonicEnchantments(IEventBus modEventBus) {
        HarmonicEnchantments.modEventBus = modEventBus;
        IHMod.validateVersion(BUILD_INFO, LOGGER);
        Initialization.init();
    }

    public static IEventBus getModEventBus() {
        return modEventBus;
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String i18n(String key) {
        return MOD_ID + "." + key;
    }

}
