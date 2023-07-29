package org.auioc.mcmod.harmonicench.common.damagesource;

import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public final class HEDamageTypes {

    public static final ResourceKey<DamageType> CURSE_OF_REBELLING = ResourceKey.create(Registries.DAMAGE_TYPE, HarmonicEnchantments.id("curse_of_rebelling"));

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(CURSE_OF_REBELLING, new DamageType("curseOfRebelling", DamageScaling.NEVER, 0.1F));
    }

}
