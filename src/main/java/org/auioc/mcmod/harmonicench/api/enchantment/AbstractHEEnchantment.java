package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.function.Predicate;
import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractHEEnchantment extends Enchantment implements IValidSlotsVisibleEnchantment {

    protected final int maxLevel;
    protected final Predicate<Enchantment> compatibility;

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlots);
        this.maxLevel = maxLevel;
        this.compatibility = compatibility;
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, Predicate<Enchantment> compatibility) {
        this(rarity, category, validSlots, 1, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel) {
        this(rarity, category, validSlots, maxLevel, (o) -> true);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots) {
        this(rarity, category, validSlots, 1, (o) -> true);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel, Predicate<Enchantment> compatibility) {
        this(rarity, category, new EquipmentSlot[] {validSlot}, maxLevel, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, Predicate<Enchantment> compatibility) {
        this(rarity, category, validSlot, 1, compatibility);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel) {
        this(rarity, category, validSlot, maxLevel, (o) -> true);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot) {
        this(rarity, category, validSlot, 1, (o) -> true);
    }

    public AbstractHEEnchantment() {
        this(Rarity.COMMON, EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND, 1, (o) -> true);
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    protected boolean checkCompatibility(Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && compatibility.test(otherEnchantment);
    }

}
