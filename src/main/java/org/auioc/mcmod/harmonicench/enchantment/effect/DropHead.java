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
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public record DropHead() implements EnchantmentEntityEffect {

    private static final DropHead INSTANCE = new DropHead();

    public static final MapCodec<DropHead> CODEC = MapCodec.unit(INSTANCE);

    private static final Map<EntityType<?>, Item> HEADS = new HashMap<>() {{
        put(EntityType.SKELETON, Items.SKELETON_SKULL);
        put(EntityType.ZOMBIE, Items.ZOMBIE_HEAD);
        put(EntityType.PIGLIN, Items.PIGLIN_HEAD);
        put(EntityType.PIGLIN_BRUTE, Items.PIGLIN_HEAD);
        put(EntityType.CREEPER, Items.CREEPER_HEAD);
        put(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL);
        // put(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD);
    }};

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof Player player) {
            var skull = new ItemStack(Items.PLAYER_HEAD);
            skull.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
            entity.spawnAtLocation(level, skull);
        } else {
            var head = HEADS.get(entity.getType());
            if (head != null) {
                entity.spawnAtLocation(level, head);
            }
        }
    }

    @Override
    public MapCodec<DropHead> codec() { return CODEC; }

}
