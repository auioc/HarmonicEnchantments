package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.arnicalib.api.game.enchantment.IValidSlotsVisibleEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractHEEnchantment extends Enchantment implements IValidSlotsVisibleEnchantment {

    protected final int maxLevel;

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel) {
        super(rarity, category, validSlots);
        this.maxLevel = maxLevel;
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots) {
        this(rarity, category, validSlots, 1);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel) {
        this(rarity, category, new EquipmentSlot[] {validSlot}, maxLevel);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot) {
        this(rarity, category, validSlot, 1);
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

}
