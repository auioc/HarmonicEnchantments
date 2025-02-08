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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.game.codec.EnumCodec;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.component.HEEDataComponents;

public record SetItemProficiency(HEEnchantedValue value, Type type) implements EnchantmentEntityEffect {

    public SetItemProficiency(HEEnchantedValue value) {
        this(value, Type.BLOCK);
    }

    public static final MapCodec<SetItemProficiency> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        HEEnchantedValue.CODEC.fieldOf("value").forGetter(o -> o.value),
        EnumCodec.byName(Type.class).optionalFieldOf("for", Type.BLOCK).forGetter(o -> o.type)
    ).apply(instance, SetItemProficiency::new));

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        var stack = item.itemStack();
        int p0 = stack.getOrDefault(HEEDataComponents.PROFICIENCY.get(), 0);
        int p = (int) value.calculate(p0, lvl, item);
        if (p0 == p) { return; }
        if (type == Type.BLOCK) {
            var pos = BlockPos.containing(origin);
            if (stack.isCorrectToolForDrops(level.getBlockState(pos))) {
                stack.set(HEEDataComponents.PROFICIENCY.get(), p);
            }
        }
        return;
    }

    @Override
    public MapCodec<SetItemProficiency> codec() { return CODEC; }

    // ============================================================================================================== //

    public enum Type { // TODO more proficiency type
        BLOCK
    }

}
