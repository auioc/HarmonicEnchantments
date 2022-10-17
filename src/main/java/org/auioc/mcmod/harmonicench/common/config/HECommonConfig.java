package org.auioc.mcmod.harmonicench.common.config;

import java.util.Comparator;
import org.auioc.mcmod.harmonicench.api.enchantment.HEEnchantmentManager;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.RegistryObject;

public class HECommonConfig {

    public static final ForgeConfigSpec CONFIG;

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("enabled");
        HEEnchantments.ENCHANTMENTS.getEntries()
            .stream()
            .map(RegistryObject::getId)
            .sorted(Comparator.comparing(ResourceLocation::getPath))
            .forEach((e) -> HEEnchantmentManager.put(e, b.define(e.getPath(), true)));
        b.pop();

        CONFIG = b.build();
    }

}
