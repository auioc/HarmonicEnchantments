/*
 * Copyright (C) 2024 AUIOC.ORG
 *
 * This file is part of HarmonicLib, a mod made for Minecraft.
 *
 * HarmonicLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.harmoniclib.advancement.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import org.auioc.mcmod.harmoniclib.core.HLRegistries;

public record HEPerformancePredicateType(Codec<? extends IHEPerformancePredicate> codec) {

    public static final Codec<IHEPerformancePredicate> CODEC = ExtraCodecs.lazyInitializedCodec(() ->
        HLRegistries.ENCHANTMENT_PERFORMANCE_PREDICATE_TYPE
            .byNameCodec()
            .dispatch("performance", IHEPerformancePredicate::getType, HEPerformancePredicateType::codec)
    );

}
