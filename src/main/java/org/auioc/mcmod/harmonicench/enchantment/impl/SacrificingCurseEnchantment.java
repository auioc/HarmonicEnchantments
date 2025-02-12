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

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.DamageEntity;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import org.auioc.mcmod.arnicalib.game.critereon.HealthPredicate;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.component.HEEDataComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantmentEffectComponents;
import org.auioc.mcmod.harmonicench.enchantment.effect.SacrificingEffect;
import org.auioc.mcmod.harmonicench.enchantment.effect.SetNumericData;

import java.util.List;

/**
 * <b>献祭诅咒 Curse of Sacrificing</b>
 * <p>
 * 需要定期杀死生物。
 * <ul>
 *     <li>每20分钟（一个游戏日）需要使用带有献祭诅咒的物品杀死一个生物，否则该物品会消失。在装备栏或副手持有也可以，杀死生物时会重置计时器。</li>
 *     <li>物品因此消失时，移除玩家所有饥饿值、饱和度，并对玩家造成3倍该物品魔咒数量的伤害（无视护甲、魔抗、附魔、抗性提升）。同时在对话框发送：“（物品）渴求祭品，在抽取了（玩家）的生命之后消失了。”</li>
 * </ul>
 */
public class SacrificingCurseEnchantment extends HEEnchantment {

    /**
     * Ⅰ:  25 - 50
     */
    private static final Cost COST = constantCost(25, 50);

    private static EnchantmentEntityEffect hurtOwner(BootstrapContext<Enchantment> ctx) {
        return new DamageEntity(
            LevelBasedValue.perLevel(4.0F), LevelBasedValue.perLevel(4.0F),
            ctx.lookup(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.GENERIC_KILL)
        );
    }

    private static final BuilderFunction BUILDER = define(
        ItemTags.WEAPON_ENCHANTABLE,
        Rarity.RARE,
        1,
        COST,
        8,
        EquipmentSlotGroup.ANY
    ).andThen((key, ctx, builder) -> builder
        .withEffect(
            HEEnchantmentEffectComponents.INVENTORY_TICK.get(),
            new SacrificingEffect(
                1200,
                HEEnchantedValue.constant(20),
                List.of() // TODO custom effect
            )
        ).withEffect(
            EnchantmentEffectComponents.POST_ATTACK,
            EnchantmentTarget.ATTACKER,
            EnchantmentTarget.ATTACKER,
            new SetNumericData<>(HEEDataComponents.SACRIFICING_PROCESS.get(), SetNumericData.Type.INT, HEEnchantedValue.constant(0)),
            LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().subPredicate(HealthPredicate.current(MinMaxBounds.Doubles.atMost(0.0D))) // TODO ?reuse
            )
        )
    );

    public static Bootstrap.Builder bootstrap() {
        return Bootstrap.of(BUILDER).curse().treasure().tradeable();
    }

}
