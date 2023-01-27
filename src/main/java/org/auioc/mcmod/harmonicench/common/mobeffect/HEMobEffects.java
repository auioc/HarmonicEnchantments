package org.auioc.mcmod.harmonicench.common.mobeffect;

import java.util.function.Supplier;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.mobeffect.impl.CollapseMobEffect;
import org.auioc.mcmod.harmonicench.common.mobeffect.impl.WeightlessnessMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HEMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HarmonicEnchantments.MOD_ID);

    private static RegistryObject<MobEffect> register(String id, Supplier<? extends MobEffect> sup) {
        return MOB_EFFECTS.register(id, sup);
    }

    public static final RegistryObject<MobEffect> WEIGHTLESSNESS = register("weightlessness", WeightlessnessMobEffect::new);
    public static final RegistryObject<MobEffect> COLLAPSE = register("collapse", CollapseMobEffect::new);

}
