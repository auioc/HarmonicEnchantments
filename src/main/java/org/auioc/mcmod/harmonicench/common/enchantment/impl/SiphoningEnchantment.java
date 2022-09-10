package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SiphoningEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.Death {

    public SiphoningEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public int getMinCost(int lvl) {
        return 15 + (lvl - 1) * 9;
    }

    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.MOB_LOOTING;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem ? true : super.canEnchant(itemStack);
    }

    @Override
    public void onLivingDeath(int lvl, LivingEntity target, DamageSource source) {
        if (!(source.getEntity() instanceof Player)) return;
        var player = (Player) source.getEntity();

        double x = target.getMaxHealth();
        double r = 0.0D;
        for (int k = 1, n = lvl + 1; k < n; k++) r += (x / 15.0D) * (1.0D / ((double) k));

        var food = player.getFoodData();
        if (food.needsFood()) {
            food.setFoodLevel(Math.min(food.getFoodLevel() + ((int) r), 20));
        } else {
            food.setSaturation(Math.min(food.getSaturationLevel() + ((float) r), food.getFoodLevel()));
        }
    }

}
