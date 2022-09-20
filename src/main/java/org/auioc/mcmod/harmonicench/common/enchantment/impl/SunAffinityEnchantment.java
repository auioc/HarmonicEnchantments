package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SunAffinityEnchantment extends AbstractHEEnchantment {

    private static final EnchantmentCategory ELYTRA = EnchantmentCategory.create("ELYTRA", (item) -> item instanceof ElytraItem);

    public SunAffinityEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            ELYTRA,
            EquipmentSlot.MAINHAND,
            3
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 * lvl;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 30;
    }

}
