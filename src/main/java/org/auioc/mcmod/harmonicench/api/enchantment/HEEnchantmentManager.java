package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class HEEnchantmentManager {

    private static final HashMap<ResourceLocation, BooleanValue> ENABLED = new HashMap<>();

    @Nullable
    public static BooleanValue getConfigEnabled(ResourceLocation id) {
        return ENABLED.get(id);
    }

    public static void put(ResourceLocation id, BooleanValue configEnabled) {
        ENABLED.put(id, configEnabled);
    }

}
