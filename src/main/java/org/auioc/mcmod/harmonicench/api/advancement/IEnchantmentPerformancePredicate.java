package org.auioc.mcmod.harmonicench.api.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.SerializationContext;

public interface IEnchantmentPerformancePredicate {

    JsonObject serializeToJson(SerializationContext conditionSerializer);

}
