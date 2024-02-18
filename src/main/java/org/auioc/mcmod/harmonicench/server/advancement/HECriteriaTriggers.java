package org.auioc.mcmod.harmonicench.server.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.server.advancement.criterion.EnchantmentPerformedTrigger;

import java.util.function.Supplier;

public final class HECriteriaTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, HarmonicEnchantments.MOD_ID);

    private static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String _id, Supplier<T> _sup) {
        return TRIGGERS.register(_id, _sup);
    }

    // ============================================================================================================== //

    public static final DeferredHolder<CriterionTrigger<?>, EnchantmentPerformedTrigger> ENCHANTMENT_PERFORMED = register("enchantment_performed", EnchantmentPerformedTrigger::new);

}
