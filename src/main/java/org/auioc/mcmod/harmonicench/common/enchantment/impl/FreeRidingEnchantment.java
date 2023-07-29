package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FreeRidingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Hurt {

    public FreeRidingEnchantment() {
        super(
            Enchantment.Rarity.VERY_RARE,
            HEnchantmentCategory.FOOD_ON_A_STACK,
            EquipmentSlot.MAINHAND,
            (o) -> o != Enchantments.MENDING && o != Enchantments.UNBREAKING
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 35;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, RandomSource random, ServerPlayer player) {
        return 0;
    }

}
