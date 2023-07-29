package org.auioc.mcmod.harmonicench;

import org.apache.logging.log4j.Logger;
import org.auioc.mcmod.arnicalib.base.log.LogUtil;
import org.auioc.mcmod.arnicalib.base.version.HVersion;
import org.auioc.mcmod.arnicalib.game.mod.HModUtil;
import org.auioc.mcmod.arnicalib.game.mod.IHMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(HarmonicEnchantments.MOD_ID)
public final class HarmonicEnchantments implements IHMod {

    public static final String MOD_ID = "harmonicench";
    public static final String MOD_NAME = "HarmonicEnchantments";

    public static final Logger LOGGER = LogUtil.getLogger(MOD_NAME);

    public static final HVersion VERSION = HModUtil.getVersion(HarmonicEnchantments.class, LOGGER);

    public HarmonicEnchantments() {
        Initialization.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String i18n(String key) {
        return MOD_ID + "." + key;
    }

}
