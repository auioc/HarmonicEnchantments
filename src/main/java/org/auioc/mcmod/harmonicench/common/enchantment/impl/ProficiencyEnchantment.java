package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class ProficiencyEnchantment extends AbstractHEEnchantment {

    public ProficiencyEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.DIGGER,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> o != Enchantments.BLOCK_EFFICIENCY
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1 + 10 * (lvl - 1);
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShearsItem ? true : super.canEnchant(itemStack);
    }

}
