/*
 * Copyright (C) 2025 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * HarmonicEnchantments is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmonicench.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.arnicalib.base.math.MathUtils;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEValueProvider;

public class HEValueProviders {

    public static final DeferredRegister<MapCodec<? extends LevelBasedValue>> TYPES = DeferredRegister.create(Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, HarmonicEnchantments.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<SigmaSum>> SIGMA_SUM = TYPES.register("sigma_sum", () -> SigmaSum.CODEC);

    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<LinearF>> LINEAR = TYPES.register("linear", () -> LinearF.CODEC);

    // ============================================================================================================== //

    /**
     * <code>∑(lvl,k=<i>lowerBound</i>)[<i>numerator</i>/<i>denominator</i>(k)]</code>
     */
    public static SigmaSum harmonic(int lowerBound, float numerator, LevelBasedValue denominator) {
        return new HEValueProviders.SigmaSum(lowerBound, new LevelBasedValue.Fraction(LevelBasedValue.constant(numerator), denominator));
    }

    /**
     * @return <code>∑(lvl,k=1)(<i>numerator</i>/<i>denominator</i>(k))</code>
     */
    public static SigmaSum harmonic(float numerator, LevelBasedValue denominator) {
        return harmonic(1, numerator, denominator);
    }

    /**
     * @return <code>∑(lvl,k=1)(1/<i>denominator</i>(k))</code>
     */
    public static SigmaSum harmonic(LevelBasedValue denominator) {
        return harmonic(1F, denominator);
    }

    /**
     * @return <code>∑(lvl,k=1)(1/<i>denominatorPerLevel</i>·k)</code>
     */
    public static SigmaSum harmonic(float denominatorPerLevel) {
        return harmonic(LevelBasedValue.perLevel(denominatorPerLevel));
    }

    /**
     * @return <code>∑(lvl,k=1)(1/k)</code>
     */
    public static SigmaSum harmonic() {
        return harmonic(LevelBasedValue.perLevel(1F));
    }

    public record SigmaSum(int lowerBound, LevelBasedValue function) implements LevelBasedValue {

        public static final MapCodec<SigmaSum> CODEC = RecordCodecBuilder.<SigmaSum>mapCodec(
            instance -> instance.group(
                Codec.INT.optionalFieldOf("lower_bound", 1).forGetter(o -> o.lowerBound),
                LevelBasedValue.CODEC.fieldOf("function").forGetter(o -> o.function)
            ).apply(instance, SigmaSum::new)
        ).validate(o -> o.lowerBound < 1 ? DataResult.error(() -> "Lower bound must be a positive integer") : DataResult.success(o));

        @Override
        public float calculate(int lvl) {
            return MathUtils.sigma(lvl, lowerBound, function::calculate);
        }

        @Override
        public MapCodec<SigmaSum> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    public static LinearF linear(float slope, float intercept) { return new LinearF(slope, intercept); }

    public static LinearF linear(float slope) { return new LinearF(slope, 0.0F); }

    public static LinearF linear() { return new LinearF(1.0F, 0.0F); }

    public record LinearF(float slope, float intercept) implements HEValueProvider {

        public static final MapCodec<LinearF> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("slope").forGetter(o -> o.slope),
            Codec.FLOAT.fieldOf("intercept").forGetter(o -> o.intercept)
        ).apply(instance, LinearF::new));

        @Override
        public float calculate(float input) {
            return slope * input + intercept;
        }

        @Override
        public MapCodec<LinearF> codec() { return CODEC; }

    }

}
