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

package org.auioc.mcmod.harmonicench.loot;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

import java.util.Optional;
import java.util.function.Consumer;

public class HELootContextParamSets {

    public static final ContextKeySet ENCHANTED_ITEM_WITH_ENTITY = register(
        "enchanted_item_with_entity",
        b -> b
            .required(LootContextParams.TOOL)
            .required(LootContextParams.ENCHANTMENT_LEVEL)
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
    );

    public static LootContext enchantedItemWithEntity(ServerLevel level, int lvl, ItemStack tool, Entity entity, Vec3 origin) {
        var params = new LootParams.Builder(level)
            .withParameter(LootContextParams.TOOL, tool)
            .withParameter(LootContextParams.ENCHANTMENT_LEVEL, lvl)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(LootContextParams.ORIGIN, origin)
            .create(ENCHANTED_ITEM_WITH_ENTITY);
        return new LootContext.Builder(params).create(Optional.empty());
    }

    public static final ContextKeySet ENCHANTED_DIRECT_ATTACK = register(
        "enchanted_direct_attack",
        b -> b
            .required(LootContextParams.TOOL)
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.ATTACKING_ENTITY)
    );

    public static LootContext enchantedDirectAttack(ServerLevel level, int lvl, ItemStack tool, Entity source, Entity target) {
        var params = new LootParams.Builder(level)
            .withParameter(LootContextParams.TOOL, tool)
            .withParameter(LootContextParams.ENCHANTMENT_LEVEL, lvl)
            .withParameter(LootContextParams.THIS_ENTITY, target)
            .withParameter(LootContextParams.ORIGIN, target.position())
            .withParameter(LootContextParams.ATTACKING_ENTITY, source)
            .create(ENCHANTED_DIRECT_ATTACK);
        return new LootContext.Builder(params).create(Optional.empty());
    }

    // ============================================================================================================== //

    private static ContextKeySet register(String path, Consumer<ContextKeySet.Builder> builder) {
        var _builder = new ContextKeySet.Builder();
        builder.accept(_builder);
        var paramSet = _builder.build();
        LootContextParamSets.REGISTRY.put(HarmonicEnchantments.id(path), paramSet);
        return paramSet;
    }

    public static void init() { }

}
