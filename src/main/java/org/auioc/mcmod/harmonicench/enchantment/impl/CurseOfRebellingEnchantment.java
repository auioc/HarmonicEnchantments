/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.random.GameRandomUtils;
import org.auioc.mcmod.harmonicench.advancement.HEEPerformancePredicates;
import org.auioc.mcmod.harmonicench.damagesource.HEDamageTypes;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.advancement.predicate.HEPerformancePredicateType;
import org.auioc.mcmod.harmoniclib.advancement.predicate.IHEPerformancePredicate;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;

import java.util.Optional;

/**
 * <b>叛逆诅咒Curse of Rebelling</b>
 * <p>
 * 物品耐久度降低时，有概率对使用者造成伤害。
 * <ul>
 *     <li>每下降1点耐久度，有1%概率对使用者造成4点伤害，无视护甲、魔抗、附魔、抗性提升。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class CurseOfRebellingEnchantment extends HLEnchantment implements IItemEnchantment.Hurt {

    public CurseOfRebellingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BREAKABLE,
            EquipmentSlot.values(),
            (o) -> o != HEEnchantments.FREE_RIDING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 25;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, RandomSource random, ServerPlayer player) {
        if (damage > 0) {
            int d = 0;
            for (int i = 0; i < damage; ++i) {
                if (GameRandomUtils.percentageChance(1, random)) {
                    d++;
                }
            }
            int value = d * 4;
            if (value > 0) {
                player.hurt(new DamageSource(player, itemStack), value);
                triggerAdvancement(player, itemStack, player.isDeadOrDying());
            }
        }
        return damage;
    }

    private void triggerAdvancement(ServerPlayer player, ItemStack itemStack, boolean isDead) {
        HLCriteriaTriggers.ENCHANTMENT_PERFORMED.get().trigger(
            player, this, itemStack,
            (PerformancePredicate p) -> p.matches(isDead)
        );
    }

    // ============================================================================================================== //

    private static class DamageSource extends net.minecraft.world.damagesource.DamageSource {

        private final ItemStack betrayedItem;

        public DamageSource(Player player, ItemStack itemStack) {
            super(
                player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(HEDamageTypes.CURSE_OF_REBELLING),
                player
            );
            this.betrayedItem = itemStack;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity living) {
            return Component.translatable("death.attack." + this.getMsgId(), living.getDisplayName(), this.betrayedItem.getDisplayName());
        }

    }

    // ============================================================================================================== //

    public record PerformancePredicate(Optional<Boolean> isDead) implements IHEPerformancePredicate {

        public boolean matches(boolean isDead) {
            return isDead().isEmpty() || isDead().get() == isDead;
        }

        // ========================================================================================================== //

        @Override
        public HEPerformancePredicateType getType() {
            return HEEPerformancePredicates.CURSE_OF_REBELLING.get();
        }

        public static final Codec<PerformancePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.BOOL, "is_dead").forGetter(PerformancePredicate::isDead)
            ).apply(instance, PerformancePredicate::new)
        );

    }

}
