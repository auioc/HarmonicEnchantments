/*
 * Copyright (C) 2024 AUIOC.ORG
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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IBlockEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.ILivingEnchantment;

import java.util.HashMap;
import java.util.Map;

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
public class HarvestEnchantment extends HLEnchantment implements ILivingEnchantment.Hurt, IBlockEnchantment.Break {

    public HarvestEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            HEnchantmentCategory.HOE,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.BLOCK_EFFICIENCY && o != HEEnchantments.PROFICIENCY.get()
        );
    }

    // Ⅰ: 15 - 61
    // Ⅱ: 24 - 71
    // Ⅲ: 33 - 81
    @Override
    public int getMinCost(int lvl) {
        return lvl * 9 + 6;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 45 + lvl;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShovelItem || super.canEnchant(itemStack);
    }

    private static final Map<EntityType<?>, Item> SKULLS = new HashMap<>() {{
        put(EntityType.SKELETON, Items.SKELETON_SKULL);
        put(EntityType.ZOMBIE, Items.ZOMBIE_HEAD);
        put(EntityType.PIGLIN, Items.PIGLIN_HEAD);
        put(EntityType.PIGLIN_BRUTE, Items.PIGLIN_HEAD);
        put(EntityType.CREEPER, Items.CREEPER_HEAD);
        put(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL);
        // put(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD);
    }};

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (isSource && !source.isIndirect() && this.isValidSlot(slot)) {
            float p = Math.max(lvl, 3) * 15.0F / 100.0F;
            float health = target.getHealth();
            if (health / target.getMaxHealth() <= p) {
                if (target instanceof Player player) {
                    var skull = new ItemStack(Items.PLAYER_HEAD);
                    var nbt = new CompoundTag();
                    nbt.putString(PlayerHeadItem.TAG_SKULL_OWNER, player.getGameProfile().getName());
                    skull.setTag(nbt);
                    target.spawnAtLocation(skull);
                } else {
                    var skull = SKULLS.get(target.getType());
                    if (skull != null) {
                        target.spawnAtLocation(skull);
                    }
                }
                return target.getMaxHealth();
            }
        }
        return amount;
    }

    @Override
    public void onBlockBreak(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos) {
        if (blockState.getBlock() instanceof CropBlock crop) {
            var level = player.level();
            int d = lvl;
            var pos1 = blockPos.offset(d, d, d);
            var pos2 = blockPos.offset(-d, -d, -d);
            for (var pos : BlockPos.betweenClosed(pos1, pos2)) {
                var state = level.getBlockState(pos);
                if (state.getBlock().equals(crop)) {
                    level.destroyBlock(pos, true, player);
                }
            }
        }
    }

}
