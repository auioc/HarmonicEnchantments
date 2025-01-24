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

package org.auioc.mcmod.harmonicench.api;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import org.auioc.mcmod.arnicalib.base.math.MathUtils;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.utils.HEHelper;

import java.util.List;
import java.util.function.Function;

public interface HEEnchantedValue {

    ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends HEEnchantedValue>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    Codec<HEEnchantedValue> DISPATCH_CODEC = REGISTRY.codec(ResourceLocation.CODEC).dispatch(HEEnchantedValue::codec, Function.identity());
    Codec<HEEnchantedValue> CODEC = Codec.either(Constant.CODEC, DISPATCH_CODEC).xmap(
        either -> either.map(HEEnchantedValue.class::cast, HEEnchantedValue.class::cast),
        value -> value instanceof Constant constant ? Either.left(constant) : Either.right(value)
    );

    static void register(String name, MapCodec<? extends HEEnchantedValue> codec) {
        REGISTRY.put(HarmonicEnchantments.id(name), codec);
    }

    static void bootstrap() {
        // register("constant", Constant.TYPED_CODEC);
        register("nested", Nested.CODEC);
        register("multiply", Multiply.CODEC);
        register("product", Product.CODEC);
        register("fraction", Fraction.CODEC);
        register("liner", Linear.CODEC);
        register("pre_level", PreLevel.CODEC);
        register("sigma_sum", SigmaSum.CODEC);
        register("enchantment_count", EnchantmentCount.CODEC);
    }

    // ============================================================================================================== //

    float calculate(float input, int lvl, EnchantedItemInUse item);

    default float calculate(int lvl, EnchantedItemInUse item) {
        return calculate(0.0F, lvl, item);
    }

    MapCodec<? extends HEEnchantedValue> codec();

    // ============================================================================================================== //

    static Constant constant(float value) { return new Constant(value); }

    record Constant(float value) implements HEEnchantedValue {

        public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);

        public static final MapCodec<Constant> TYPED_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("value").forGetter(o -> o.value)
        ).apply(instance, Constant::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return this.value;
        }

        @Override
        public MapCodec<Constant> codec() { return TYPED_CODEC; }

    }

    // ============================================================================================================== //

    static Nested nested(HEEnchantedValue... values) { return new Nested(List.of(values)); }

    record Nested(List<HEEnchantedValue> values) implements HEEnchantedValue {

        public static final MapCodec<Nested> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.listOf().fieldOf("values").forGetter(o -> o.values)
        ).apply(instance, Nested::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            float output = input;
            for (var value : values) {
                output = value.calculate(output, lvl, item);
            }
            return output;
        }

        @Override
        public MapCodec<Nested> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static Multiply multiply(HEEnchantedValue factor) { return new Multiply(factor); }

    record Multiply(HEEnchantedValue factor) implements HEEnchantedValue {

        public static final MapCodec<Multiply> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.fieldOf("factor").forGetter(o -> o.factor)
        ).apply(instance, Multiply::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return input * factor.calculate(input, lvl, item);
        }

        @Override
        public MapCodec<Multiply> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static Product product(HEEnchantedValue... factors) { return new Product(List.of(factors)); }

    record Product(List<HEEnchantedValue> factors) implements HEEnchantedValue {

        public static final MapCodec<Product> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.listOf().fieldOf("factors").forGetter(o -> o.factors)
        ).apply(instance, Product::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            float output = 1.0F;
            for (var factor : factors) {
                output = output * factor.calculate(input, lvl, item);
            }
            return output;
        }

        @Override
        public MapCodec<Product> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static Fraction fraction(HEEnchantedValue numerator, HEEnchantedValue denominator) { return new Fraction(numerator, denominator); }

    record Fraction(HEEnchantedValue numerator, HEEnchantedValue denominator) implements HEEnchantedValue {

        public static final MapCodec<Fraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                HEEnchantedValue.CODEC.fieldOf("numerator").forGetter(o -> o.numerator),
                HEEnchantedValue.CODEC.fieldOf("denominator").forGetter(o -> o.denominator)
            ).apply(instance, Fraction::new)
        );

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            float d = denominator.calculate(input, lvl, item);
            if (d == 0.0F) {
                return 0.0F;
            }
            float n = numerator.calculate(input, lvl, item);
            return n / d;
        }

        @Override
        public MapCodec<Fraction> codec() { return CODEC; }

    }


    // ============================================================================================================== //

    static Linear linear(float slope, float intercept) { return new Linear(constant(slope), constant(intercept)); }

    static Linear linear(float slope) { return new Linear(constant(slope), constant(0.0F)); }

    static Linear linear() { return new Linear(constant(1.0F), constant(0.0F)); }


    /**
     * <code>output = <i>slope</i> * <b>input</b> + <i>intercept</i></code>
     */
    record Linear(HEEnchantedValue slope, HEEnchantedValue intercept) implements HEEnchantedValue {

        public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.optionalFieldOf("slope", new Constant(1.0F)).forGetter(o -> o.slope),
            HEEnchantedValue.CODEC.optionalFieldOf("intercept", new Constant(0.0F)).forGetter(o -> o.intercept)
        ).apply(instance, Linear::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return slope.calculate(input, lvl, item) * input + intercept.calculate(input, lvl, item);
        }

        @Override
        public MapCodec<Linear> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static PreLevel perLevel(float base, float perLevelAfterFirst) { return new PreLevel(base, perLevelAfterFirst); }

    static PreLevel perLevel(float perLevel) { return new PreLevel(perLevel, perLevel); }

    /**
     * <code>output = <i>perLevelAboveFirst</i> * (<b>lvl</b> - 1)  + <i>base</i></code>
     */
    record PreLevel(float base, float perLevelAboveFirst) implements HEEnchantedValue {

        public static final MapCodec<PreLevel> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("base").forGetter(o -> o.base),
            Codec.FLOAT.fieldOf("per_level_above_first").forGetter(o -> o.perLevelAboveFirst)
        ).apply(instance, PreLevel::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return base + perLevelAboveFirst * (float) (lvl - 1);
        }

        @Override
        public MapCodec<PreLevel> codec() { return CODEC; }

    }

    // ============================================================================================================== //


    /**
     * <code>∑(<b>lvl</b>,k=<i>lowerBound</i>)[<i>numerator</i>/<i>denominator</i>(k,<b>lvl</b>)]</code>
     */
    static SigmaSum harmonic(int lowerBound, float numerator, HEEnchantedValue denominator) {
        return new SigmaSum(lowerBound, fraction(constant(numerator), denominator));
    }

    /**
     * @return <code>∑(<b>lvl</b>,k=1)(<i>numerator</i>/<i>denominator</i>(k,<b>lvl</b>))</code>
     */
    static SigmaSum harmonic(float numerator, HEEnchantedValue denominator) {
        return harmonic(1, numerator, denominator);
    }

    /**
     * @return <code>∑(<b>lvl</b>,k=1)(1/<i>denominator</i>(k,<b>lvl</b>))</code>
     */
    static SigmaSum harmonic(HEEnchantedValue denominator) {
        return harmonic(1.0F, denominator);
    }

    /**
     * @return <code>∑(<b>lvl</b>,k=1)(1/k)</code>
     */
    static SigmaSum harmonic() { return harmonic(linear()); }


    /**
     * <code>∑(<b>lvl</b>,k=<i>lowerBound</i>)[<i>function</i>(k,<b>lvl</b>)]</code>
     */
    record SigmaSum(int lowerBound, HEEnchantedValue function) implements HEEnchantedValue {

        public static final MapCodec<SigmaSum> CODEC = RecordCodecBuilder.<SigmaSum>mapCodec(
            instance -> instance.group(
                Codec.INT.optionalFieldOf("lower_bound", 1).forGetter(o -> o.lowerBound),
                HEEnchantedValue.CODEC.fieldOf("function").forGetter(o -> o.function)
            ).apply(instance, SigmaSum::new)
        ).validate(o -> o.lowerBound < 1 ? DataResult.error(() -> "Lower bound must be a positive integer") : DataResult.success(o));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return MathUtils.sigma(lvl, lowerBound, (int k) -> function.calculate((float) k, lvl, item));
        }

        @Override
        public MapCodec<SigmaSum> codec() { return CODEC; }

    }

    // ============================================================================================================== //

    static EnchantmentCount countEnchantments() { return new EnchantmentCount(); }

    record EnchantmentCount() implements HEEnchantedValue {

        public static final MapCodec<EnchantmentCount> CODEC = MapCodec.unit(EnchantmentCount::new);

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return HEHelper.getAllEnchantments(item.itemStack()).size();
        }

        @Override
        public MapCodec<EnchantmentCount> codec() { return CODEC; }

    }

}
