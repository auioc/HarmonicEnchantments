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

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record DestroyBlock(List<Vec3i> offset, Optional<BlockPredicate> predicate, boolean drop) implements EnchantmentEntityEffect {

    private static final Codec<List<Vec3i>> OFFSET_CODEC = Codec.either(Vec3i.CODEC, Vec3i.CODEC.listOf()).xmap(
        either -> either.map(List::of, Function.identity()),
        value -> value.size() == 1 ? Either.left(value.getFirst()) : Either.right(value)
    );

    public static final MapCodec<DestroyBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        OFFSET_CODEC.fieldOf("offset").forGetter(o -> o.offset),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(o -> o.predicate),
        Codec.BOOL.optionalFieldOf("drop", true).forGetter(o -> o.drop)
    ).apply(instance, DestroyBlock::new));

    public DestroyBlock(List<Vec3i> offset, BlockPredicate predicate) {
        this(offset, Optional.of(predicate), true);
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        var originPos = BlockPos.containing(origin);
        for (var offset : offset()) {
            var pos = originPos.offset(offset);
            if (predicate.isPresent() && predicate.get().test(level, pos)) {
                level.destroyBlock(pos, drop, entity);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() { return CODEC; }

}
