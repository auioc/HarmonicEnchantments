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

package org.auioc.mcmod.harmonicench.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.game.codec.EnumCodec;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;

import java.util.function.UnaryOperator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public record SetNumericData<T extends Number>(DataComponentType<T> component, Type type, HEEnchantedValue value) implements EnchantmentEntityEffect {

    public static final MapCodec<SetNumericData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        DataComponentType.CODEC.fieldOf("component").forGetter(o -> o.component),
        EnumCodec.byNameLowerCase(Type.class).fieldOf("number").forGetter(o -> o.type), // TODO ?name
        HEEnchantedValue.CODEC.fieldOf("value").forGetter(o -> o.value)
    ).apply(instance, (c, t, v) -> new SetNumericData(c, t, v))); // TODO check with codec

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        var stack = item.itemStack();
        Number p0 = stack.getOrDefault(component, 0);
        float p = value.calculate(p0.floatValue(), lvl, item);
        if (p0.floatValue() != p) {
            stack.set((DataComponentType<Number>) component, type.cast(p));
        }
    }

    @Override
    public MapCodec<SetNumericData> codec() { return CODEC; }

    // ============================================================================================================== //

    public enum Type {

        BYTE(Number::byteValue),
        SHORT(Number::shortValue),
        INT(Number::intValue),
        LONG(Number::longValue),
        FLOAT(Number::floatValue),
        DOUBLE(Number::doubleValue);

        private final UnaryOperator<Number> f;

        Type(UnaryOperator<Number> f) {
            this.f = f;
        }

        public Number cast(Float v) {
            return f.apply(v);
        }

    }

}
