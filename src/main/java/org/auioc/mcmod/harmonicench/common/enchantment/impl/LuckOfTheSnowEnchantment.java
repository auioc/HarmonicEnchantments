package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class LuckOfTheSnowEnchantment extends AbstractHEEnchantment implements IItemEnchantment.FishingRod {

    public LuckOfTheSnowEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.FISHING_ROD, EquipmentSlot.values(), 3);
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
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FISHING_LUCK;
    }

    @Override
    public Pair<Integer, Integer> preFishingRodCast(int lvl, ItemStack fishingRod, ServerPlayer player, Level level, int speedBonus, int luckBonus) {
        float temperature = level.getBiome(player.blockPosition()).value().getBaseTemperature();
        if (temperature <= 0.0F) {
            luckBonus += lvl * 2;
        } else if (temperature <= 0.2F) {
            luckBonus += lvl;
        }
        return Pair.of(speedBonus, luckBonus);
    }

    @Override
    public void postFishingRodCast(int lvl, ItemStack fishingRod, FishingHook fishingHook, ServerPlayer player, Level level) {}

}
