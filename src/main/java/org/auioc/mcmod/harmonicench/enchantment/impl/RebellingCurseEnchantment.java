/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.DamageEntity;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.enchantment.effects.RemoveBinomial;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.effect.ItemDamagedEffect;

import java.util.function.Function;

/**
 * <b>叛逆诅咒 Curse of Rebelling</b>
 * <p>
 * 物品耐久度降低时，有概率对使用者造成伤害。
 * <ul>
 *     <li>每下降1点耐久度，有1%概率对使用者造成4点伤害，无视护甲、魔抗、附魔、抗性提升。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class RebellingCurseEnchantment extends HEEnchantment {

    private static final EnchantmentTagBuilder EXCLUSIVE = exclusiveSet(
        (tag) -> tag.add() // TODO HEEnchantments.FREE_RIDING
    );

    /**
     * Ⅰ:  2 - 50
     */
    private static final Cost COST = constantCost(2, 50);

    private static final EnchantmentValueEffect PROCESS_ITEM_DELTA_DAMAGE = new RemoveBinomial(LevelBasedValue.constant(0.99F));
    private static final Function<BootstrapContext<Enchantment>, EnchantmentEntityEffect> HURT_OWNER = (ctx) -> new DamageEntity(
        LevelBasedValue.perLevel(4.0F), LevelBasedValue.perLevel(4.0F),
        ctx.lookup(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.GENERIC_KILL)
    );

    private static final BuilderFunction BUILDER = define(
        ItemTags.DURABILITY_ENCHANTABLE,
        EXCLUSIVE,
        Rarity.RARE,
        1,
        COST,
        4,
        EquipmentSlotGroup.ANY
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.ITEM_DAMAGED.get(),
            new ItemDamagedEffect(PROCESS_ITEM_DELTA_DAMAGE, HURT_OWNER.apply(ctx))
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).tag(EXCLUSIVE).curse().treasure().tradeable();
    }

}
