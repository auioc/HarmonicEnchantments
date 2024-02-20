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

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentTagUtils;
import org.auioc.mcmod.harmoniclib.enchantment.EnchantmentHelper;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IItemEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <b>献祭诅咒Curse of Sacrificing</b>
 * <p>
 * 需要定期杀死生物。
 * <ul>
 *     <li>每20分钟（一个游戏日）需要使用带有献祭诅咒的物品杀死一个生物，否则该物品会消失。在装备栏或副手持有也可以，杀死生物时会重置计时器。</li>
 *     <li>物品因此消失时，给予玩家负等级的生命提升效果，持续时间为该物品所有魔咒等级之和，效果等级为魔咒总数。同时在对话框发送：“（物品）渴求祭品，在抽取了（玩家）的生命之后消失了。”</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class CurseOfSacrificingEnchantment extends HLEnchantment implements IItemEnchantment.Tick.Inventory, ILivingEnchantment.Death, IItemEnchantment.Tooltip {

    private static final String NBT_TIME = "SacrificingProcess";
    private static final int MAX_TIME = 20 * 60;

    public CurseOfSacrificingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.VANISHABLE,
            EquipmentSlot.values(),
            1
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 15 + (lvl - 1) * 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean isTreasureOnly() { return true; }

    @Override
    public boolean isCurse() { return true; }

    @Override
    public void onInventoryTick(int lvl, ItemStack itemStack, Player player, Level level, int index, boolean selected) {
        if (player.tickCount % 5 == 0 && !player.getAbilities().instabuild && !player.isSpectator()) {
            var nbt = itemStack.getOrCreateTag();
            int time = nbt.getInt(NBT_TIME);
            if (time >= MAX_TIME && !level.isClientSide) {
                var enchTag = itemStack.getEnchantmentTags();
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, (EnchantmentTagUtils.calcTotalLevel(enchTag) * 60) * 20, (-1 * enchTag.size()) - 1));
                player.hurt(player.damageSources().magic(), 0.1F);
                player.sendSystemMessage(Component.translatable(getDescriptionId() + ".vanished", itemStack.getDisplayName(), player.getDisplayName()));
                itemStack.setCount(0);
            } else if (player.tickCount % 20 == 0) {
                nbt.putInt(NBT_TIME, time + lvl);
            }
        }
    }

    @Override
    public void onLivingDeath(int lvl, ItemStack itemStack, LivingEntity target, DamageSource source) {
        if (source.getEntity() instanceof Player) {
            resetSacrificingProcess(itemStack);
        }
    }

    public static int getSacrificingProcess(ItemStack itemStack) {
        return (itemStack.hasTag()) ? itemStack.getTag().getInt(NBT_TIME) : 0;
    }

    public static void resetSacrificingProcess(ItemStack itemStack) {
        if (itemStack.hasTag()) itemStack.getTag().putInt(NBT_TIME, 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(int lvl, @NotNull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        EnchantmentHelper.getEnchantmentTooltip(lines, this.getDescriptionId()).ifPresent(
            (text) -> {
                int time = getSacrificingProcess(itemStack);
                if (time > 0 && time <= MAX_TIME) {
                    ((MutableComponent) text).append(String.format(" (%s)", StringUtil.formatTickDuration((MAX_TIME - time) * 20, 20))); // TODO
                }
            }
        );
    }

}
