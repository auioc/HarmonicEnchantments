package org.auioc.mcmod.harmonicench.common.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.auioc.mcmod.harmonicench.api.enchantment.HEEnchantmentManager;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;

import java.util.Comparator;

public class HECommonConfig {

    public static final ModConfigSpec CONFIG;

    static {
        final ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.push("enabled");
        HEEnchantments.ENCHANTMENTS.getEntries()
            .stream()
            .map(DeferredHolder::getId)
            .sorted(Comparator.comparing(ResourceLocation::getPath))
            .forEach((e) -> HEEnchantmentManager.put(e, b.define(e.getPath(), true)));
        b.pop();

        CONFIG = b.build();
    }

}
