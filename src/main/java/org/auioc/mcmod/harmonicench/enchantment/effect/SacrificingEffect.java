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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;
import org.auioc.mcmod.harmonicench.component.HEEDataComponents;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.utils.HEHelper;

import java.util.List;

public record SacrificingEffect(int ticksPreProgress, HEEnchantedValue progressThreshold, List<EnchantmentEntityEffect> effects) implements EnchantmentEntityEffect {

    public static final MapCodec<SacrificingEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("ticks_pre_progress").forGetter(o -> o.ticksPreProgress),
        HEEnchantedValue.CODEC.fieldOf("progress_threshold").forGetter(o -> o.progressThreshold),
        EnchantmentEntityEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(o -> o.effects) // TODO custom effect
    ).apply(instance, SacrificingEffect::new));

    private static final DataComponentType<Integer> DATA_COMPONENT = HEEDataComponents.SACRIFICING_PROCESS.get();
    private static final String MESSAGE_KEY = "enchantment.harmonicench.sacrificing_curse.vanished";

    @Override
    public void apply(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof ServerPlayer player
            && player.tickCount % ticksPreProgress == 0
            && !player.getAbilities().instabuild
            && !player.isSpectator()
        ) {
            var stack = item.itemStack();
            int p = stack.getOrDefault(DATA_COMPONENT, 0);
            if (p >= progressThreshold.calculate(lvl, item)) {
                effects.forEach(effect -> effect.apply(level, lvl, item, player, origin));
                player.hurtServer(level, player.damageSources().genericKill(), 3.0F * HEHelper.getAllEnchantments(stack).size());
                player.getFoodData().setFoodLevel(0);
                player.getFoodData().setSaturation(0F);
                player.sendSystemMessage(Component.translatable(MESSAGE_KEY, stack.getDisplayName(), player.getDisplayName()));
                stack.setCount(0);
            } else {
                stack.set(DATA_COMPONENT, p + 1);
            }
        }
    }

    @Override
    public MapCodec<SacrificingEffect> codec() { return CODEC; }

    // ============================================================================================================== //

    private static final String NAME_KEY = Util.makeDescriptionId("enchantment", HEEnchantments.SACRIFICING_CURSE.location());

    @OnlyIn(Dist.CLIENT)
    public static void addToTooltip(ItemStack stack, List<Component> tooltip) { // TODO ?better way to add sacrificing process
        int p = stack.getOrDefault(HEEDataComponents.SACRIFICING_PROCESS.get(), 0);
        if (p > 0) {
            HEHelper.getEnchantmentTooltip(tooltip, NAME_KEY).ifPresent(text -> {
                ((MutableComponent) text).append(String.format(" (%s)", p));
            });
        }
    }

}
