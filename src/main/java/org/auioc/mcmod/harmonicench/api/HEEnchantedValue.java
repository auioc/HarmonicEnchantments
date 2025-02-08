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
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import org.auioc.mcmod.arnicalib.base.math.MathUtils;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.utils.HEHelper;

import java.util.List;
import java.util.Optional;
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
        register("identity", Identity.CODEC);
        register("nested", Nested.CODEC);
        register("sum", Sum.CODEC);
        register("multiply", Multiply.CODEC);
        register("product", Product.CODEC);
        register("fraction", Fraction.CODEC);
        register("liner", Linear.CODEC);
        register("chance", Chance.CODEC);
        register("level", Level.CODEC);
        register("pre_level", PreLevel.CODEC);
        register("sigma_sum", SigmaSum.CODEC);
        register("enchantment_count", EnchantmentCount.CODEC);
        register("total_enchantment_level", TotalEnchantmentLevel.CODEC);
    }

    // ============================================================================================================== //

    float calculate(float input, int lvl, EnchantedItemInUse item);

    default float calculate(int lvl, EnchantedItemInUse item) {
        return calculate(0.0F, lvl, item);
    }

    MapCodec<? extends HEEnchantedValue> codec();

    // ============================================================================================================== //

    static Constant constant(float value) { return new Constant(value); }

    static Constant constant(int value) { return new Constant((float) value); }

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

    static Identity identity() { return new Identity(); }

    record Identity() implements HEEnchantedValue {

        public static final MapCodec<Identity> CODEC = MapCodec.unit(Identity::new);

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return input;
        }

        @Override
        public MapCodec<Identity> codec() { return CODEC; }

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

    static Sum sum(HEEnchantedValue... summands) { return new Sum(List.of(summands)); }

    record Sum(List<HEEnchantedValue> summands) implements HEEnchantedValue {

        public static final MapCodec<Sum> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HEEnchantedValue.CODEC.listOf().fieldOf("summands").forGetter(o -> o.summands)
        ).apply(instance, Sum::new));

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            float output = 0.0F;
            for (var summand : summands) {
                output += summand.calculate(input, lvl, item);
            }
            return output;
        }

        @Override
        public MapCodec<Sum> codec() { return CODEC; }

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

    static Chance chance(HEEnchantedValue probability, HEEnchantedValue value, HEEnchantedValue or) { return new Chance(probability, value, or); }

    static Chance chance(HEEnchantedValue probability, HEEnchantedValue value) { return chance(probability, value, constant(0)); }

    static Chance chance(float probability, float value) { return chance(constant(probability), constant(value)); }

    record Chance(HEEnchantedValue probability, HEEnchantedValue value, HEEnchantedValue or) implements HEEnchantedValue {

        public static final MapCodec<Chance> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                HEEnchantedValue.CODEC.fieldOf("probability").forGetter(o -> o.probability),
                HEEnchantedValue.CODEC.fieldOf("value").forGetter(o -> o.value),
                HEEnchantedValue.CODEC.optionalFieldOf("or", constant(0)).forGetter(o -> o.or)
            ).apply(instance, Chance::new)
        );

        private static final RandomSource RANDOM = RandomSource.create();

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            var p = Optional.ofNullable(item.owner()).map(Entity::getRandom).orElse(RANDOM).nextFloat();
            var b = probability.calculate(input, lvl, item);
            return p < b ? value.calculate(input, lvl, item) : or.calculate(input, lvl, item);
        }

        @Override
        public MapCodec<Chance> codec() { return CODEC; }

    }


    // ============================================================================================================== //

    static Level level() { return new Level(); }

    record Level() implements HEEnchantedValue {

        public static final MapCodec<Level> CODEC = MapCodec.unit(Level::new);

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return lvl;
        }

        @Override
        public MapCodec<Level> codec() { return CODEC; }

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

    static SigmaSum harmonic(HEEnchantedValue upperBound, HEEnchantedValue lowerBound, HEEnchantedValue numerator, HEEnchantedValue denominator) {
        return new SigmaSum(upperBound, lowerBound, fraction(numerator, denominator));
    }

    static SigmaSum harmonic(HEEnchantedValue upperBound, HEEnchantedValue lowerBound, float numerator, HEEnchantedValue denominator) {
        return harmonic(upperBound, lowerBound, constant(numerator), denominator);
    }

    static SigmaSum harmonic(HEEnchantedValue upperBound, float numerator, HEEnchantedValue denominator) {
        return harmonic(upperBound, constant(1), constant(numerator), denominator);
    }

    /**
     * <code>∑(<i>upperBound</i>(<b>input</b>,<b>lvl</b>),k=<i>lowerBound(<b>input</b>,<b>lvl</b>)</i>)[<i>function</i>(k,<b>lvl</b>)]</code>
     */
    record SigmaSum(HEEnchantedValue upperBound, HEEnchantedValue lowerBound, HEEnchantedValue function) implements HEEnchantedValue {

        public static final MapCodec<SigmaSum> CODEC = RecordCodecBuilder.<SigmaSum>mapCodec(
            instance -> instance.group(
                HEEnchantedValue.CODEC.optionalFieldOf("upper_bound", constant(1)).forGetter(o -> o.upperBound),
                HEEnchantedValue.CODEC.optionalFieldOf("lower_bound", constant(1)).forGetter(o -> o.lowerBound),
                HEEnchantedValue.CODEC.fieldOf("function").forGetter(o -> o.function)
            ).apply(instance, SigmaSum::new)
        );

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return MathUtils.sigma(
                (int) upperBound.calculate(input, lvl, item),
                (int) lowerBound.calculate(input, lvl, item),
                (int k) -> function.calculate((float) k, lvl, item)
            );
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

    // ============================================================================================================== //

    static TotalEnchantmentLevel totalLevel() { return new TotalEnchantmentLevel(); }

    record TotalEnchantmentLevel() implements HEEnchantedValue {

        public static final MapCodec<TotalEnchantmentLevel> CODEC = MapCodec.unit(TotalEnchantmentLevel::new);

        @Override
        public float calculate(float input, int lvl, EnchantedItemInUse item) {
            return HEHelper.getAllEnchantments(item.itemStack()).entrySet().stream()
                .mapToInt(Object2IntMap.Entry::getIntValue).sum();
        }

        @Override
        public MapCodec<TotalEnchantmentLevel> codec() { return CODEC; }

    }

}
