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
    public static final HashMap<ResourceLocation, BooleanValue> FUNCTIONALITY = new HashMap<>();

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        var ids = HEEnchantments.ENCHANTMENTS.getEntries().stream().map(RegistryObject::getId).toList();

        b.push("acquirability");
        ids.forEach((e) -> ACQUIRABILITY.put(e, b.define(e.getPath(), true)));
        b.pop();

        b.push("functionality");
        ids.forEach((e) -> FUNCTIONALITY.put(e, b.define(e.getPath(), true)));
        b.pop();

        CONFIG = b.build();
    }

}
