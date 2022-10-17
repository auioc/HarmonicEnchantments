package org.auioc.mcmod.harmonicench.common.config;

import java.util.HashMap;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.registries.RegistryObject;

public class HECommonConfig {

    public static final ForgeConfigSpec CONFIG;

    public static final HashMap<ResourceLocation, BooleanValue> ACQUIRABILITY = new HashMap<>();

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("acquirability");
        HEEnchantments.ENCHANTMENTS.getEntries()
            .stream()
            .map(RegistryObject::getId)
            .forEach((e) -> {
                ACQUIRABILITY.put(e, b.define(e.toString().replace(":", "."), true));
            });
        b.pop();

        CONFIG = b.build();
    }

}
