/*
 * Copyright (C) 2024-2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.enchantment.impl;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.SetValue;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.auioc.mcmod.arnicalib.game.critereon.HealthPredicate;
import org.auioc.mcmod.arnicalib.game.loot.predicate.EnchantmentLevelCondition;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.effect.DestroyBlock;
import org.auioc.mcmod.harmonicench.enchantment.effect.DropHead;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>收割 Harvest</b>
 * <p>
 * 提高收割作物（以及生物）的速度。
 * <ul>
 *     <li>成功挖掘农作物时，会同时挖掘切比雪夫距离 1/2/3 格内的同种农作物（更高等级维持3格）。</li>
 *     <li>攻击生命值在 15%/30%/45% 以下的目标时，将直接杀死目标并获得对应的头颅（更高等级维持45%）。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.1
 */
public class HarvestEnchantment extends HEEnchantment {

    private static final ItemTagBuilder SUPPORTED_ITEMS = supportedItems((tag) -> tag.addTags(
        ItemTags.HOES, ItemTags.SHOVELS
    ));

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        Enchantments.EFFICIENCY, HEEnchantments.PROFICIENCY
    );

    /**
     * Ⅰ: 15 - 61 <br>
     * Ⅱ: 24 - 71 <br>
     * Ⅲ: 33 - 81 <br>
     */
    private static final Cost COST = dynamicCost(15, 9, 61, 9);

    private static final BuilderFunction BUILDER = define(
        SUPPORTED_ITEMS,
        ItemTags.HOES,
        EXCLUSIVE,
        Rarity.VERY_RARE,
        3,
        COST,
        1,
        EquipmentSlotGroup.HAND
    ).andThen((key, ctx, builder) -> {
            builder.withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new DropHead(),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().subPredicate(HealthPredicate.current(MinMaxBounds.Doubles.atMost(0.0D)))
                )
            );
            killIf(builder, MinMaxBounds.Ints.exactly(1), MinMaxBounds.Doubles.atMost(0.15D));
            killIf(builder, MinMaxBounds.Ints.exactly(2), MinMaxBounds.Doubles.atMost(0.30D));
            killIf(builder, MinMaxBounds.Ints.atLeast(3), MinMaxBounds.Doubles.atMost(0.45D));
            destroyCropsIf(ctx, builder, MinMaxBounds.Ints.exactly(1), 1);
            destroyCropsIf(ctx, builder, MinMaxBounds.Ints.exactly(2), 2);
            destroyCropsIf(ctx, builder, MinMaxBounds.Ints.atLeast(3), 3);
            return builder;
        }
    );

    private static void killIf(Enchantment.Builder builder, MinMaxBounds.Ints lvl, MinMaxBounds.Doubles healthPercent) {
        builder.withEffect(
            EnchantmentEffectComponents.DAMAGE,
            new SetValue(LevelBasedValue.constant(Float.MAX_VALUE)),
            AllOfCondition.allOf(
                EnchantmentLevelCondition.of(lvl),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().subPredicate(HealthPredicate.percent(healthPercent))
                )
            )
        );
    }

    private static void destroyCropsIf(BootstrapContext<Enchantment> ctx, Enchantment.Builder builder, MinMaxBounds.Ints lvl, int d) {
        var pos1 = BlockPos.ZERO.offset(d, 0, d);
        var pos2 = BlockPos.ZERO.offset(-d, 0, -d);
        List<Vec3i> offsets = new ArrayList<>(4 * d * d + 4 * d + 1);
        for (var pos : BlockPos.betweenClosed(pos1, pos2)) {
            if (!BlockPos.ZERO.equals(pos)) {
                offsets.add(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
            }
        }
        builder.withEffect(
            HEEnchantmentEffectComponents.BLOCK_DESTROYED.get(),
            new DestroyBlock(offsets, BlockPredicate.matchesTag(BlockTags.CROPS)),
            AllOfCondition.allOf(
                EnchantmentLevelCondition.of(lvl),
                LocationCheck.checkLocation(LocationPredicate.Builder.location()
                    .setBlock(net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(lookupBlock(ctx), BlockTags.CROPS))
                )
            )
        );
    }

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(SUPPORTED_ITEMS, EXCLUSIVE).tradeable();
    }

}
