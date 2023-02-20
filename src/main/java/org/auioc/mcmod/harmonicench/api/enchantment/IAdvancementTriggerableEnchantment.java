package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;

public interface IAdvancementTriggerableEnchantment {

    IEnchantmentPerformancePredicate deserializePerformancePredicate(JsonObject json, DeserializationContext conditionParser);

}
