package org.auioc.mcmod.harmonicench.common.mobeffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.mobeffect.impl.ConfusionMobEffect;
import org.auioc.mcmod.harmonicench.common.mobeffect.impl.WeightlessnessMobEffect;

import java.util.function.Supplier;

public final class HEMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HarmonicEnchantments.MOD_ID);

    private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String id, Supplier<T> sup) {
        return MOB_EFFECTS.register(id, sup);
    }

    public static final DeferredHolder<MobEffect, WeightlessnessMobEffect> WEIGHTLESSNESS = register("weightlessness", WeightlessnessMobEffect::new);
    public static final DeferredHolder<MobEffect, ConfusionMobEffect> CONFUSION = register("confusion", ConfusionMobEffect::new);

}
