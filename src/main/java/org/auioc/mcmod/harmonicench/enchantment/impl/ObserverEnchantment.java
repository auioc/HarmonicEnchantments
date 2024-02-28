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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.TickEvent;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.random.GameRandomUtils;
import org.auioc.mcmod.arnicalib.game.world.phys.RayCastUtils;
import org.auioc.mcmod.arnicalib.game.world.position.PositionUtils;
import org.auioc.mcmod.harmonicench.data.HETags;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmoniclib.enchantment.api.HLEnchantment;
import org.auioc.mcmod.harmoniclib.enchantment.api.IPlayerEnchantment;

/**
 * <b>观测 Observer</b>
 * <p>
 * 在晴朗的夜晚，了解天象。
 * <ul>
 *     <li>使用望远镜持续 10（5）秒后，在对话框报告距离下次雨或雷暴的时间。</li>
 *     <li>触发时，有 5%（10%）的概率，在对话框报告玩家 64 格内最近的可定位结构（<code>{@linkplain HETags#DOWSING_LOCATABLE #harmonicench:dowsing_locatable}</code>）。</li>
 *     <li>生效限制：必须处于晴朗的夜晚，玩家在正上方没有视野遮挡，仰角不少于60度，且准心不能指向任何方块或实体。</li>
 * </ul>
 *
 * TODO en_us
 *
 * @author WakelessSloth56
 * @author Libellule505
 * @since 2.1.0
 */
public class ObserverEnchantment extends HLEnchantment implements IPlayerEnchantment.Tick {

    public ObserverEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            HEnchantmentCategory.SPYGLASS,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND },
            2,
            (o) -> o != HEEnchantments.AIM.get()
        );
    }

    // Ⅰ: 10 - 25
    // Ⅱ: 20 - 35
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
    public void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, TickEvent.Phase phase, LogicalSide side) {
        if (phase != TickEvent.Phase.END || side != LogicalSide.SERVER) return;

        var level = (ServerLevel) player.level();
        if (
            player.isScoping()
                && player.getTicksUsingItem() == (lvl == 1 ? 10 : 5) * 20
                && player.getXRot() <= -60.0F
                && level.isNight()
                && !level.isRaining()
                && !level.isThundering()
                && RayCastUtils.onView(player, player.getBlockReach(), (r) -> 1, (r) -> 2) < 0
                && PositionUtils.iterateY(player.getBlockX(), player.getBlockZ(), player.getBlockY(), level.getMaxBuildHeight(),
                (pos) -> level.getBlockState(pos).getLightBlock(level, pos) < 15
            )
        ) {
            reportWeather(player, level);
            if (GameRandomUtils.percentageChance(lvl * 5, player.getRandom())) {
                dowseStructure(player, level);
            }
        }
    }

    private void reportWeather(Player player, ServerLevel level) {
        var data = (ServerLevelData) level.getLevelData();

        int r = data.getRainTime();
        int t = data.getThunderTime();
        int c = data.getClearWeatherTime();
        int f = 2;
        if (r == t) {
            r = c;
            t = c;
            f = 1;
        }

        float tps = level.tickRateManager().tickrate();
        player.sendSystemMessage(Component.empty()
            .append(Component.translatable(
                this.getDescriptionId() + ".weather." + f,
                player.getDisplayName(),
                StringUtil.formatTickDuration(t, tps),
                StringUtil.formatTickDuration(r, tps)
            ))
        );
    }

    private void dowseStructure(Player player, ServerLevel level) {
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        var structures = registry.getTag(HETags.DOWSING_LOCATABLE);
        if (structures.isEmpty()) return;

        var srcPos = player.blockPosition();
        var result = level.getChunkSource()
            .getGenerator()
            .findNearestMapStructure(level, structures.get(), srcPos, 64, false);

        var pos = result.getFirst();
        player.sendSystemMessage(Component.empty()
            .append(Component.translatable(
                this.getDescriptionId() + ".structure",
                player.getDisplayName(),
                pos.getX(), pos.getZ(), distance(srcPos, pos),
                result.getSecond().unwrapKey().get().location().toString()
            ))
        );
    }

    private static int distance(BlockPos pos1, BlockPos pos2) {
        int x = pos2.getX() - pos1.getX();
        int z = pos2.getZ() - pos1.getZ();
        return (int) Math.floor(Math.sqrt((float) (x * x + z * z)));
    }

}
