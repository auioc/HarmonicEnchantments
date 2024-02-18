package org.auioc.mcmod.harmonicench.api.advancement;

import com.mojang.serialization.Codec;

public record EPPredicateType(Codec<? extends IEnchantmentPerformancePredicate> codec) {
}
