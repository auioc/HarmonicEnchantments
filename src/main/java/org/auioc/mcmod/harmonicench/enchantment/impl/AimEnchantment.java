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
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.TickEvent;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.world.phys.RayCastUtils;
import org.auioc.mcmod.harmonicench.advancement.HEEPerformancePredicates;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.advancement.predicate.HEPerformancePredicateType;
import org.auioc.mcmod.harmoniclib.advancement.predicate.IHEPerformancePredicate;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IPlayerEnchantment;

import java.util.Optional;


/**
 * <b>瞄准 Aim</b>
 * <p>
 * 使用望远镜时，标记敌对生物并提高弹射物伤害。
 * <ul>
 *     <li>每 5/3 秒，给予准心指向的敌对生物发光效果，持续 <code>∑(n,k=1)(20/k)</code> 秒，同时在对话框发送：“发现（生物），距离（玩家）X米。”</li>
 *     <li>使用或手持望远镜时，弹射物对带有发光效果的生物造成伤害增加 <code>[∑(n,k=1)(1/3k)]×100%</code>。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class AimEnchantment extends HLEnchantment implements ILivingEnchantment.Hurt, IPlayerEnchantment.Tick {

    public AimEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.SPYGLASS,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND },
            2
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return lvl * 10;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 15;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (
            isSource
                && source.is(DamageTypeTags.IS_PROJECTILE)
                && target.hasEffect(MobEffects.GLOWING)
                && source.getEntity() instanceof Player player
                && (player.isScoping() || player.getOffhandItem().is(Items.SPYGLASS) || player.getMainHandItem().is(Items.SPYGLASS))
        ) {
            return amount + (float) MathUtil.sigma(lvl, 1, (double i) -> (1.0D / (3.0D * i)));
        }
        return amount;
    }

    @Override
    public void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, TickEvent.Phase phase, LogicalSide side) {
        if (phase == TickEvent.Phase.END && side == LogicalSide.SERVER && player.tickCount % 11 == 0 && player.isScoping()) {
            var hit = RayCastUtils.entityOnView(player, 100.0D);
            if (hit != null && hit.getEntity() instanceof LivingEntity living) {
                if (!living.hasEffect(MobEffects.GLOWING)) {
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, MathUtil.sigma(lvl, 1, (int i) -> (20 / i)) * 20, 1));
                    player.sendSystemMessage(
                        Component.translatable(
                            getDescriptionId() + ".looking_at",
                            living.getDisplayName(), player.getDisplayName(), Math.round(living.position().distanceTo(player.position()))
                        )
                    );
                    triggerAdvancement((ServerPlayer) player, itemStack, living);
                }
            }
        }
    }

    private void triggerAdvancement(ServerPlayer player, ItemStack itemStack, LivingEntity aimedEntity) {
        HLCriteriaTriggers.ENCHANTMENT_PERFORMED.get().trigger(
            player, this, itemStack,
            (PerformancePredicate p) -> p.matches(player, aimedEntity)
        );
    }

    // ============================================================================================================== //

    public record PerformancePredicate(Optional<ContextAwarePredicate> aimedEntity, Optional<DistancePredicate> distance) implements IHEPerformancePredicate {

        public PerformancePredicate(EntityPredicate aimedEntityPredicate) {
            this(Optional.of(EntityPredicate.wrap(aimedEntityPredicate)), Optional.empty());
        }

        public boolean matches(ServerPlayer player, LivingEntity aimedLiving) {
            return (aimedEntity.isEmpty()
                || aimedEntity.get().matches(EntityPredicate.createContext(player, aimedLiving))
            )
                && (distance.isEmpty()
                || distance.get().matches(player.getX(), player.getY(), player.getZ(), aimedLiving.getX(), aimedLiving.getY(), aimedLiving.getZ())
            );
        }

        // ========================================================================================================== //

        @Override
        public HEPerformancePredicateType getType() {
            return HEEPerformancePredicates.AIM.get();
        }

        public static final Codec<PerformancePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "aimed_entity").forGetter(PerformancePredicate::aimedEntity),
                ExtraCodecs.strictOptionalField(DistancePredicate.CODEC, "distance").forGetter(PerformancePredicate::distance)
            ).apply(instance, PerformancePredicate::new)
        );

    }

}
