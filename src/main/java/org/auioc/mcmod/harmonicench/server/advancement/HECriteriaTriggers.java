package org.auioc.mcmod.harmonicench.server.advancement;

import org.auioc.mcmod.harmonicench.server.advancement.criterion.EnchantmentPerformedTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public final class HECriteriaTriggers {

    public static final EnchantmentPerformedTrigger ENCHANTMENT_PERFORMED = register(new EnchantmentPerformedTrigger());

    // ====================================================================== //

    public static void init() {}

    private static <T extends CriterionTrigger<?>> T register(T instance) {
        return CriteriaTriggers.register(instance);
    }

}
